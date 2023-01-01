package br.com.vtsantos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BuscaImdb {

	public static void main(String[] args) {
		String apiKey = null;
		boolean pegaKey = false;
		List<String> ListaDeTermosDeBusca = new ArrayList<String>();
		for (String texto : args) {
			if (pegaKey) {
				apiKey = texto;
				pegaKey = false;
			} else if (texto.toLowerCase().equals("--apikey") && apiKey == null) {
				pegaKey = true;
			} else {
				ListaDeTermosDeBusca.add(texto);
			}
		}
		if (apiKey == null) {
			throw new IllegalArgumentException("parametro --apikey eh obrigatorio");
		}
		System.out.println("Parametros recebidos: --apikey " + apiKey);
		String[] termosDeBusca = ListaDeTermosDeBusca.toArray(new String[ListaDeTermosDeBusca.size()]);
		System.out.print("Buscando IMDB por (" + termosDeBusca.length +"): ");
		for(int i = 0 ; i < termosDeBusca.length ; i++) {
			System.out.print("'" + termosDeBusca[i] + "' ");
		}
		System.out.println();
		ApiImdbFactory apiImbdb = new ApiImdbFactory();
		String urlPesquisa = apiImbdb.getUrlPesquisa(apiKey, termosDeBusca);
		System.out.println("URL de pesquisa: " + urlPesquisa);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlPesquisa)).build();

		HttpResponse<String> response = null;
		try {
			response = client.send(request, BodyHandlers.ofString());
			System.out.print("Resultados encontrados:");
			System.out.println("(HttpStatusCode: " + response.statusCode() + ")");
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

		JSONObject json = new JSONObject(response.body());
		System.out.println("Json recebido como resposta:");
		System.out.println(json.toString(4));
		json = new JSONObject(json, "results");

		JSONArray arrayJson = json.getJSONArray("results");

		System.out.println("Tratamento do Json para captura do titulo, descricao e url da imagem:");
		List<String> titulos = ConverteJsonParaLista(arrayJson, "title");
		List<String> images = ConverteJsonParaLista(arrayJson, "image");
//		List<String> titulos = ConverteJsonParaLista(arrayJson, "title");
		
		for (int i = 0; i < arrayJson.length(); i++) {
			JSONObject jsonItem = (JSONObject) arrayJson.get(i);
			System.out.println("----------------------------");
			System.out.println(jsonItem.getString("title") + " : " + jsonItem.getString("description"));
			if ((jsonItem.getString("image") != null) && (!jsonItem.getString("image").trim().equals(""))) {
				System.out.println(jsonItem.get("image"));
			}
		}
		
		System.out.println(titulos);
		System.out.println(images);
		
		System.out.println("Fim");

	}
	
	public static List<String> ConverteJsonParaLista (JSONArray jsonArray, String termo){
		List<String> resultado = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonItem = (JSONObject) jsonArray.get(i);
			resultado.add(jsonItem.getString(termo));
		}
		return resultado;

	}
}


