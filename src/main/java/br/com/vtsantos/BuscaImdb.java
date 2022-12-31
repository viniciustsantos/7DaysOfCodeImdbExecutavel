package br.com.vtsantos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuscaImdb {

	public static void main(String[] args) {
		System.out.println("Buscando IMDB por:");
		String key = null;
		boolean pegaKey = false;
		List<String> ListaDeTermosDeBusca = new ArrayList<String>();
		for(String texto : args) {
			if (pegaKey) {
				key = texto;
				pegaKey = false ;
			} else if (texto.toLowerCase().equals("--apikey") && key == null) {
				pegaKey = true;
			} else {
				ListaDeTermosDeBusca.add(texto);
			}
			System.out.println(texto);	
		}
		if (key == null) {
			throw new IllegalArgumentException("parametro --apikey eh obrigatorio");
		}
		String[] termosDeBusca = ListaDeTermosDeBusca.toArray(new String[ListaDeTermosDeBusca.size()]);
		ApiImdbFactory apiImbdb = new ApiImdbFactory();
		String urlPesquisa = apiImbdb.getUrlPesquisa(key, termosDeBusca);
		System.out.println("URL de pesquisa: " + urlPesquisa);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
		      .uri(URI.create(urlPesquisa))
		      .build();
		System.out.println("Resultados encontrados:");
		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.statusCode());
		} catch ( InterruptedException | IOException e) {
			e.printStackTrace();
		}
  
		JSONObject json = new JSONObject(response.body());
		System.out.println(json.toString(4));
		System.out.println("Fim");

	}
}

