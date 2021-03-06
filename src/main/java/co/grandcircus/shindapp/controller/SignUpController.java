package co.grandcircus.shindapp.controller;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.grandcircus.shindapp.dao.ItemDao;
import co.grandcircus.shindapp.dao.SignupDao;
import co.grandcircus.shindapp.model.Allergen;
import co.grandcircus.shindapp.model.Ingredient;
import co.grandcircus.shindapp.model.Item;
import co.grandcircus.shindapp.model.Signup;

@Controller
public class SignUpController {

	private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

	@Autowired
	private SignupDao signupDao;
	
	@Autowired
	private ItemDao itemDao;

	/*
	 * Get method which loads the sign-up page, calling on the database to store the information in objects for use by the view.
	 */
	@RequestMapping(value = "/sign-up", method = RequestMethod.GET)
	public String listSignupView(Model model) {
		
		
		model.addAttribute("signup", new Signup());
		
		List<Signup> signupList = signupDao.getAllSignup();
		List<int[]> listOfDishAllergens = new ArrayList<>();
		for(Signup signup: signupList){
			
			try {
				signup.setItem(signupDao.getSignup(signup.getId()).getItem());
				int[] allergens = itemDao.getAllergens(signup);
				listOfDishAllergens.add(allergens);
				signup.setAllergenList(allergens);
				
				model.addAttribute("list", signupList);
				
			} catch (NameNotFoundException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		model.addAttribute("allergenList", listOfDishAllergens);
		System.out.println("/signup -> sign-up.jsp");
		System.out.println(listOfDishAllergens);
		return "sign-up";
	}

	/*
	 * Post method which adds a new sign-up to the signup table.  The information put into the db table comes from the HTML form in the page.
	 */
	@RequestMapping(value = "/sign-up", method = RequestMethod.POST)
	public String listSignup(Model model, Signup signup, RedirectAttributes redirectAttrs) {

		model.addAttribute("list", signupDao.getAllSignup());
		signupDao.addSignup(signup);
		redirectAttrs.addFlashAttribute("showAll", true);
		String returnStatement = "redirect:/item-info?id=";
		try {
			returnStatement += signupDao.getSignupId(signup);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStatement;
	}

	/*
	 * Post method that deletes a user by calling upon the Dao to delete the specified row in the signup table.
	 */
	
	@RequestMapping(value = "/sign-up/{id}/delete", method = RequestMethod.POST)
	public String deleteMovie(@PathVariable int id, Model model) {
		try {
			signupDao.deleteSignup(id);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.asMap().clear();

		logger.info("POST /sign-up/" + id + "/delete -> redirect to /sign-up");
		return "redirect:/sign-up";
	}

	
	/*
	 * Test method that is not currently being used.
	 */
	@RequestMapping(value = "/sign-up-test", method = RequestMethod.GET)
	public String listSignupTestView(Model model) {
		model.addAttribute("list", signupDao.getAllSignup());
		model.addAttribute("signup", new Signup());
		List<ArrayList> listOfDishAllergens = new ArrayList<>();
		for(Signup signup: signupDao.getAllSignup()){
			
			try {
				signup.setItem(signupDao.getSignup(signup.getId()).getItem());
				listOfDishAllergens.add(signupDao.getSignup(signup.getId()).getItem().getAllergens());
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		model.addAttribute("allergenList", listOfDishAllergens);
		System.out.println("/signup -> sign-up.jsp");
		return "sign-up-test";
	}
	
	/*
	 * Get method that loads a page for a specific user.
	 */
//	@RequestMapping(value = "/sign-up/{id}", method = RequestMethod.GET)
//	public String getSignup(@PathVariable int id, Signup signup, Model model) {
//		try {
//			model.addAttribute("list", signupDao.getAllSignup());
//			model.addAttribute("signup", signupDao.getSignup(id));
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		model.addAttribute("id", id);
//
//		logger.info("GET /sign-up/" + id + " -> sign-up.jsp");
//		return "sign-up";
//	}

	/*
	 * Post method that updates the information for a user.
	 */
//	@RequestMapping(value = "/sign-up/{id}", method = RequestMethod.POST)
//	public String saveSignup(@PathVariable int id, Signup signup, Model model) {
//		try {
//			signupDao.updateSignup(signup);
//			model.addAttribute("list", signupDao.getAllSignup());
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		model.addAttribute("id", id);
//
//		logger.info("POST /sign-up/" + id + " -> sign-up.jsp");
//		
//		return "sign-up";
//	}

}