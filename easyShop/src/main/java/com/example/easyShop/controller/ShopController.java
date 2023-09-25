package com.example.easyShop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.easyShop.model.BuyProduct;
import com.example.easyShop.model.Product;
import com.example.easyShop.model.UserRegister;
import com.example.easyShop.service.ProductPurchaseService;
import com.example.easyShop.service.ProductService;
import com.example.easyShop.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ProductService productService;

	@Autowired
	ProductPurchaseService productPurchaseService;

	@GetMapping(value = "/login")
	public ModelAndView home() {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

	@GetMapping(value = "/newUserRegistration")
	public ModelAndView userRegistration() {

		UserRegister user = new UserRegister();
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", user);
		mv.setViewName("registration");
		return mv;
	}

	@PostMapping(value = "/newUserRegistration/doRegister")
	public String doRegister(@ModelAttribute("user") UserRegister userBackingBean,
			RedirectAttributes redirectAttributes, HttpSession session) {
		String email = userBackingBean.getEmail();
		String mobile = userBackingBean.getMobile();
		boolean emailExist = userService.isEmailExist(email);
		boolean mobileExist = userService.isMobileNumberExist(mobile);
		if (!emailExist && !mobileExist) {
			String rawPassword = userBackingBean.getPassword();
			userBackingBean.setPassword(passwordEncoder.encode(rawPassword));
			userService.registerUser(userBackingBean);
			session.setAttribute("user", email);
			/*
			 * redirectAttributes.addFlashAttribute("email", email);
			 * redirectAttributes.addFlashAttribute("password",
			 * userBackingBean.getPassword());
			 */
			redirectAttributes.addAttribute("email", email);
			redirectAttributes.addAttribute("password", rawPassword);
			return "redirect:/doLogin";
		} else {
			if (emailExist)
				redirectAttributes.addFlashAttribute("email_Exist_Err_Msg",
						"Email already exist. Please retry with a different Email.");
			if (mobileExist)
				redirectAttributes.addFlashAttribute("mobile_Exist_Err_Msg",
						"Mobile Number already exist. Please retry with a different Mobile Number.");
			return "redirect:/newUserRegistration";
		}

	}

	// @PostMapping(value = "/doLogin")
	@RequestMapping(value = "/doLogin", method = { RequestMethod.POST, RequestMethod.GET })
	public String doLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			RedirectAttributes redirectAttributes, HttpSession session) {

		if (redirectAttributes.getAttribute("email") != null)
			email = (String) redirectAttributes.getAttribute("email");
		if (redirectAttributes.getAttribute("password") != null)
			password = (String) redirectAttributes.getAttribute("password");

		String encryptedPassword = passwordEncoder.encode(password);

		boolean isUserValid = userService.validateUser(email, encryptedPassword);
		if (!isUserValid) {
			redirectAttributes.addFlashAttribute("login_Err_Msg", "Email or Password is Incorrect");
			return "redirect:/login";
		} else {
			UserRegister user = userService.getUserDetails(email);
			session.setAttribute("user", user.getEmail());
			StringBuilder sb = new StringBuilder();
			sb.append(user.getFirstName());
			sb.append(" ");
			sb.append(user.getLastName());
			session.setAttribute("User_Name", sb.toString());
			if (user != null && "BUYER".equalsIgnoreCase(user.getUserType()))
				return "redirect:/buyerHome";
			else if (user != null && "SELLER".equalsIgnoreCase(user.getUserType()))
				return "redirect:/sellerHome";
		}
		return "redirect:/login";
	}

	@GetMapping(value = "/sellerHome")
	public ModelAndView sellerHome(HttpSession session, RedirectAttributes redirectAttributes) {

		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("user") == null) {
			mv.addObject("login_Err_Msg", "Invalid Session. Please login again");
			mv.setViewName("login");
			return mv;
		}
		String userName = "";
		if (session.getAttribute("User_Name") != null) {
			userName = (String) session.getAttribute("User_Name");
		}
		List<Product> productList = new ArrayList<Product>();
		mv.setViewName("sellerHome");
		String user = "";

		user = (String) session.getAttribute("user");
		List<Product> allListedProduct = productService.getAllProductListedBySeller(user);
		mv.addObject("productList", allListedProduct);
		mv.addObject("userName", userName);
		UserRegister buyerUser = userService.getUserDetails(user);
		if (buyerUser != null) {
			mv.addObject("balance", buyerUser.getBalance());
		}
		return mv;
	}

	@GetMapping(value = "/newProduct")
	public String addNewProduct(Model model, HttpSession session, RedirectAttributes redirectAttributes) {

		if (session.getAttribute("user") == null) {
			redirectAttributes.addFlashAttribute("login_Err_Msg", "Invalid Session. Please login again");
			return "redirect:/login";
		}
		Product product = new Product();
		model.addAttribute("product", product);
		return "newProduct";
	}

	@PostMapping(value = "/addNewProduct")
	public String saveNewProduct(@ModelAttribute("product") Product productBean, RedirectAttributes redirectAttributes,
			HttpSession session) {

		String user = "";
		if (session.getAttribute("user") != null) {
			user = (String) session.getAttribute("user");
		}
		boolean isProductSaved = productService.saveNewProduct(productBean, user);
		if (isProductSaved) {
			redirectAttributes.addFlashAttribute("productSaveSuccessMsg", "One product added successfully");
		}
		if (!isProductSaved) {
			redirectAttributes.addFlashAttribute("productSaveFailMsg", "Product not added. Please retry");
		}
		return "redirect:/sellerHome";
	}

	@GetMapping(value = "/deleteProduct/{id}")
	public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		boolean isProductDeleted = productService.deleteProductById(id);
		if (isProductDeleted) {
			redirectAttributes.addFlashAttribute("deleteSuccessMsg", "1 product deleted successfully");
		}
		if (!isProductDeleted) {
			redirectAttributes.addFlashAttribute("deleteFailedMsg", "Failed to delete product");
		}
		return "redirect:/sellerHome";
	}

	@GetMapping(value = "/updateProduct/{id}")
	public ModelAndView updateProductView(@PathVariable("id") Long id) {

		ModelAndView mv = new ModelAndView();
		Product product = productService.getProductDtlByProductId(id);
		mv.addObject("product", product);
		mv.setViewName("updateProduct");
		return mv;
	}

	@PostMapping(value = "/updateProductDtl")
	public String updateProduct(@ModelAttribute("product") Product productBean, RedirectAttributes redirectAttributes,
			HttpSession session) {

		if (session.getAttribute("user") == null) {
			redirectAttributes.addFlashAttribute("login_Err_Msg", "Invalid Session. Please login again");
			return "redirect:/login";
		}
		boolean isProductUpdated = productService.updateProductDtl(productBean);
		if (isProductUpdated) {
			redirectAttributes.addFlashAttribute("updateSuccessMsg",
					"Product name " + productBean.getProductName() + " updated successfully");
		}
		if (!isProductUpdated) {
			redirectAttributes.addFlashAttribute("updateFailedMsg", "Failed to update product");
		}
		return "redirect:/sellerHome";
	}

	@GetMapping(value = "/buyerHome")
	public ModelAndView buyerHome(HttpSession session, RedirectAttributes redirectAttributes) {

		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("user") == null) {
			mv.addObject("login_Err_Msg", "Invalid Session. Please login again");
			mv.setViewName("login");
			return mv;
		}
		String userName = "";
		if (session.getAttribute("User_Name") != null) {
			userName = (String) session.getAttribute("User_Name");
		}
		List<Product> productList = new ArrayList<Product>();
		mv.setViewName("buyerHome");
		String user = "";
		if (session.getAttribute("user") != null) {
			user = (String) session.getAttribute("user");
		}
		BuyProduct buy = new BuyProduct();
		List<Product> allProductForSell = productService.getAllProductListedForSell();
		mv.addObject("productList", allProductForSell);
		mv.addObject("userName", userName);
		mv.addObject("buyProduct", buy);

		UserRegister buyerUser = userService.getUserDetails(user);
		if (buyerUser != null) {
			mv.addObject("balance", buyerUser.getBalance());
		}

		return mv;
	}

	
	@GetMapping(value = "/purchase/{param}")
	public String purchaseProduct(@PathVariable("param") String pathVariable, HttpSession session,
			RedirectAttributes redirectAttributes) {

		if (session.getAttribute("user") == null) {
			redirectAttributes.addFlashAttribute("login_Err_Msg", "Invalid Session. Please login again");
			return "redirect:/login";
		}
		/*
		 * String[] pathVariableArr = pathVariable.split("&"); Long productId =
		 * Long.parseLong(pathVariableArr[0]); Integer buyUnits =
		 * Integer.parseInt(pathVariableArr[1]);
		 */
		Long productId = Long.parseLong(pathVariable);
		String buyerUser = (String) session.getAttribute("user");
		Product productDtl = productService.getProductDtlByProductId(productId);
		//productDtl.setBuyUnits(buyUnits.intValue());
		productDtl.setBuyUnits(1);
		productPurchaseService.purchaseProductAndPerformCalculation(productDtl, buyerUser);
		return "redirect:/purchaseHistory";
	}
	 
	/*
	 * @PostMapping(value = "/purchase") public String
	 * purchaseProduct(@ModelAttribute("buyProduct") BuyProduct product, HttpSession
	 * session, RedirectAttributes redirectAttributes) {
	 * 
	 * if (session.getAttribute("user") == null) {
	 * redirectAttributes.addFlashAttribute("login_Err_Msg",
	 * "Invalid Session. Please login again"); return "redirect:/login"; } String
	 * pathVariable = ""; String[] pathVariableArr = pathVariable.split("&"); Long
	 * productId = Long.parseLong(pathVariableArr[0]); Integer buyUnits =
	 * Integer.parseInt(pathVariableArr[1]); String buyerUser = (String)
	 * session.getAttribute("user"); Product productDtl =
	 * productService.getProductDtlByProductId(productId);
	 * productDtl.setBuyUnits(buyUnits.intValue());
	 * productPurchaseService.purchaseProductAndPerformCalculation(productDtl,
	 * buyerUser); return "redirect:/buyerHome"; }
	 */

	@GetMapping(value = "/purchaseHistory")
	public ModelAndView buyerPurchaseHistory(HttpSession session) {

		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("user") == null) {
			mv.addObject("login_Err_Msg", "Invalid Session. Please login again");
			mv.setViewName("login");
			return mv;
		}
		mv.setViewName("purchase");
		String user = "";

		user = (String) session.getAttribute("user");

		List<Product> puchasedProducts = productPurchaseService.getAllProductsPurchasedByBuyer(user);
		mv.addObject("productList", puchasedProducts);
		return mv;
	}

	@GetMapping(value = "/logout")
	public String logOut(HttpSession session) {
		session.removeAttribute("user");
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping(value = "/saleHistory")
	public ModelAndView saleHistory(HttpSession session) {

		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("user") == null) {
			mv.addObject("login_Err_Msg", "Invalid Session. Please login again");
			mv.setViewName("login");
			return mv;
		}
		mv.setViewName("sale");
		String user = "";

		String seller = (String) session.getAttribute("user");
		List<Product> soldProducts = productPurchaseService.getSaleHistoryOfSeller(seller);
		mv.addObject("productList", soldProducts);
		return mv;
	}
	
	@GetMapping(value = "/error")
	public ModelAndView errorPage(HttpSession session) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("error");
		return mv;
	}
}
