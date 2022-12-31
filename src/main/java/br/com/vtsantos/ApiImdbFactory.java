package br.com.vtsantos;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ApiImdbFactory {

	private String apiKey = null;

	public String getApiKey() {
		return apiKey;
	}

	public String getUrlPesquisa(String key) {
		if (key == null) {
			throw new IllegalArgumentException("ApiKey nao pode ser nulo");
		}
		this.apiKey = key;
		
		return montaUrlPesquisa() ;
	}

	
	public String getUrlPesquisa(String key, String[] termosDeBusca) {
		if (key == null) {
			throw new IllegalArgumentException("ApiKey nao pode ser nulo");
		}
		this.apiKey = key;
		return montaUrlPesquisa(termosDeBusca) ;
		
	}

	public String getUrlPesquisa(String[] termosDeBusca) {
		if (this.apiKey == null) {
			throw new RuntimeException("ApiKey ainda nao foi definida");
		}

		return montaUrlPesquisa(termosDeBusca) ;
	}
	
	private String montaUrlPesquisa(String[] termosDeBusca) {
		if (termosDeBusca == null || termosDeBusca.length < 1) {
			return montaUrlPesquisa();
		}
		String urlBase = "https://imdb-api.com/pt-BR/API/Search/" + this.apiKey + "/";
		String stringPesquisa = "";
		for(String termo : termosDeBusca) {
			stringPesquisa = stringPesquisa + termo;
		}	
		try {
			return urlBase + URLEncoder.encode(stringPesquisa , "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return urlBase + stringPesquisa;
		}
	}

	private String montaUrlPesquisa() {
		return "https://imdb-api.com/en/API/Top250Movies/" + this.apiKey + "/";
	}
	
}
