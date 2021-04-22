package request.receive;

import BDDconnection.BDDConnection;
import KeywordsExctractor.KeywordsExtractor;
import KeywordsExctractor.Langage;
import Utils.SQLUtils;
import request.GenericRequest;
import request.GenericRequestInterface;
import server.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchRequest extends GenericRequest implements GenericRequestInterface {
	private String query;
	private int limitFrom;
	private int limitTo;

	@Override
	public void handle(Client client) {
		if (!client.isAuthentified()) {
			client.respond("not connected");
			return;
		}

		KeywordsExtractor keywordsExtractor = new KeywordsExtractor(Langage.FRENCH);
		ArrayList<String> keywords = keywordsExtractor.getKeywords(query);

		System.out.println(Arrays.toString(keywords.toArray()));

		StringBuilder queryBuilder = new StringBuilder();

		boolean isFirstWhere = true;

		for (int i = 0; i < keywords.size(); i++) {
			queryBuilder.append(isFirstWhere ? "WHERE" : "OR");
			queryBuilder.append(" Titre LIKE ? ");
			isFirstWhere = false;
			queryBuilder.append(isFirstWhere ? "WHERE" : "OR");
			queryBuilder.append(" Libelle LIKE ? ");
		}

		Connection conn = BDDConnection.getConnection();
		try {
			PreparedStatement prepareStatement = conn.prepareStatement(
					"SELECT * FROM Image WHERE Id_Image IN ( SELECT DISTINCT Image.Id_Image FROM Image LEFT JOIN Possede ON Image.Id_Image = Possede.Id_Image LEFT JOIN Tag ON Tag.Id_Tag = Possede.Id_Tag " + queryBuilder
							.toString() + " ) LIMIT ?, ?");

			for (int i = 0; i < keywords.size(); i++) {
				prepareStatement.setString(i * 2 + 1, "%" + keywords.get(i) + "%");
				prepareStatement.setString(i * 2 + 2, "%" + keywords.get(i) + "%");
			}

			prepareStatement.setInt(keywords.size() * 2 + 1, limitFrom);
			prepareStatement.setInt(keywords.size() * 2 + 2, limitTo);

			System.out.println(prepareStatement.toString());

			ResultSet resultSet = prepareStatement.executeQuery();
			System.out.println(SQLUtils.printResultSet(resultSet));

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}