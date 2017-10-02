/**
 * @author Anil Tilve
 * @author Ayush Joshi
 */


package views;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Song;

public class AlterLibraryDialog extends Stage {

	ObservableList<Song> songs;
	ListView listView;

	public AlterLibraryDialog(Stage owner, String mode, ListView<Song> listView) {
		this.listView = listView;

		this.initOwner(owner);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(5); //horizontal gap in pixels => that's what you are asking for
		gridPane.setVgap(5); //vertical gap in pixels
		gridPane.setPadding(new Insets(5, 5, 5, 5)); //margins around the whole grid

		Scene childScene = new Scene(gridPane);
		if (mode.equals("Add")){
			this.setTitle(mode + " New Song");
		}
		else
		{
			this.setTitle(mode + " Song");
		}

		this.setScene(childScene);

		Label nameLabel = new Label("Name:"),
				artistLabel = new Label("Artist:"), 
				albumLabel = new Label("Album:"),
				yearLabel = new Label("Year:");

		TextField nameField = new TextField(), 
				artistField = new TextField(), 
				albumField = new TextField(),
				yearField = new TextField();
		
		Song selectedSong = listView.getSelectionModel().getSelectedItem();
		
		if (mode.equals("Edit")) {
			nameField.setText(selectedSong.getName());
			artistField.setText(selectedSong.getArtist());
			albumField.setText(selectedSong.getAlbum());
			yearField.setText(selectedSong.getYear());
		}
		
		Button confirmButton = new Button("Confirm " + mode);
		
		gridPane.add(nameLabel, 0, 0);
		gridPane.add(nameField, 0, 1);
		gridPane.add(artistLabel, 0, 2);
		gridPane.add(artistField, 0, 3);
		gridPane.add(albumLabel, 0, 4);
		gridPane.add(albumField, 0, 5);
		gridPane.add(yearLabel, 0, 6);
		gridPane.add(yearField, 0, 7);
		gridPane.add(confirmButton, 0, 9);

		this.show();

		confirmButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String enteredName = nameField.getText(),
						enteredArtist = artistField.getText();
				
				if (enteredName.isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Text Input Error");
					alert.setHeaderText("No Song Name Present Error");
					alert.setContentText("The song name is required.");

					alert.showAndWait();
					return;
				}
				else if(!isString(enteredName))
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Text Input Error");
					alert.setHeaderText("Song Name error.");
					alert.setContentText("Song name must be String.");

					alert.showAndWait();
					return;

				}
				else if (enteredArtist.isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Text Input Error");
					alert.setHeaderText("No Artist Present error.");
					alert.setContentText("Artist is required.");

					alert.showAndWait();
					return;
				}
				else if(!isString(enteredArtist))
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Text Input Error");
					alert.setHeaderText("Artist Name error.");
					alert.setContentText("Artist must be String.");

					alert.showAndWait();
					return;

				}
				
				for (Song song : listView.getItems()) {
					System.out.println(song.getName() + " " + song.getArtist());
					System.out.println(enteredName + " "+enteredArtist);
					if (song.compareTo(selectedSong) == 0)
						continue;
					if (enteredName.equals(song.getName()) && enteredArtist.equals(song.getArtist())) {
						System.out.println("I am here");
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Song " + mode + " Error");
						alert.setHeaderText("Error" + mode + "ing");
						alert.setContentText("Another song with the same name and artist already exists in the library.");

						alert.showAndWait();
						return;
					}
				}
				
				String enteredAlbum = albumField.getText(),
						enteredYear = yearField.getText();
				
				if (mode.equals("Add")) {
					Song newSong = new Song(enteredName, enteredArtist, enteredAlbum, enteredYear);
					listView.getItems().add(newSong);
					FXCollections.sort(listView.getItems());
					listView.getSelectionModel().select(newSong);
				}
				else {
					listView.getSelectionModel().getSelectedItem().setName(enteredName);
					listView.getSelectionModel().getSelectedItem().setArtist(enteredArtist);
					listView.getSelectionModel().getSelectedItem().setAlbum(enteredAlbum);
					listView.getSelectionModel().getSelectedItem().setYear(enteredYear);
					listView.refresh();
				}
				
				BufferedWriter writer = null;
				
				try {
					writer = new BufferedWriter(new FileWriter("testsongs.json"));
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					gson.toJson(listView.getItems().toArray(new Song[listView.getItems().size()]), writer);
				} catch (FileNotFoundException exception) {
					exception.printStackTrace();
				} catch (IOException exception) {
					exception.printStackTrace();
				} finally {
					try {
						writer.close();
					} catch (IOException exception) {
						exception.printStackTrace();
					}
				}
				
				childScene.getWindow().hide();
			}
		});
	}


	public boolean isString( String input )
	{
		try
		{
			for(int i =0;i<input.length();i++)
			{
				if(Character.isAlphabetic(input.charAt(i))){
					continue;
				}
				else
				{
					return false;
				}
			}
			return true;
		}
		catch( Exception e)
		{
			return false;
		}
	}
}
