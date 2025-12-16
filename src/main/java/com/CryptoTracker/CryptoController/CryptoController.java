package com.CryptoTracker.CryptoController;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CryptoTracker.CryptoService.CryptoService;
import com.CryptoTracker.model.CryptoCoins;

@Controller
public class CryptoController {
	
	@Autowired
	private CryptoService cryptoService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@PostMapping("/track")
	public String track(@RequestParam String coins, Model model) {

	    List<String> coinList = Arrays.stream(coins.split(","))
	            .map(String::trim)
	            .map(String::toLowerCase)
	            .toList();

	    List<CryptoCoins> result = cryptoService.getCryptoPrices(coinList);

	    model.addAttribute("coins", result);
	    return "result";
	}

}
