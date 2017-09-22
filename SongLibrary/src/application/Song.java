/**
 * @author Anil Tilve
 * @author Ayush Joshi
 */

package application;

public class Song {
	
	private String name,
		artist,
		album,
		year;
	
	public Song(String name, String artist, String album, String year) {
		this.setName(name);
		this.setName(artist);
		this.setName(album);
		this.setName(year);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public void setName(String name) {
		if (name.isEmpty()) {
			
		}
		else
			this.name = name;
	}
	
	public void setArtist(String artist) {
		if (artist.isEmpty()) {
			
		}
		else
			this.artist = artist;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
}
