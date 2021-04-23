package request.send;

import java.util.ArrayList;

public class SearchResponse extends GenericResponse {
	ArrayList<ImageResponse> images;

	public SearchResponse(ArrayList<ImageResponse> images) {
		this.images = images;
	}
}