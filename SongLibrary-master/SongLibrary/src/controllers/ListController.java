/**
 * @author Anil Tilve
 * @author Ayush Joshi
 */

package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.util.Callback;

import models.Song;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import views.AlterLibraryDialog;

public class ListController {

	@FXML
	ListView<Song> listView;
	@FXML
	Button addButton;
	@FXML
	Button editButton;
	@FXML
	Button deleteButton;

	private ObservableList<Song> songs;
	private BufferedWriter writer;
	private Gson gson;

	public void start(Stage mainStage) throws IOException {
		BufferedReader reader = null;

		try {
			gson = new GsonBuilder().setPrettyPrinting().create();
			reader = new BufferedReader(new FileReader("testsongs.json"));
			listView.setItems(FXCollections.observableArrayList(gson.fromJson(reader, Song[].class)));
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		} finally {
			reader.close();
		}
		FXCollections.sort(listView.getItems()); 

		listView.getSelectionModel().select(0);
		
		showSelectedSongDetails(listView.getSelectionModel().getSelectedItem());
		listView.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue,
				newValue) -> showSelectedSongDetails(listView.getSelectionModel().getSelectedItem()));

		addButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				AlterLibraryDialog dialog = new AlterLibraryDialog(mainStage, "Add", listView);
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (listView.getItems().isEmpty())
					return;
				
				Song selectedSong = listView.getSelectionModel().getSelectedItem();
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Delete Confirmation Dialog");
				alert.setHeaderText("Confirm deletion");
				alert.setContentText("Are you sure you want to delete \"" + selectedSong.getName()
				+ " - " + selectedSong.getArtist() + "\"?");
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					alert.close();
					int selectedIndex = listView.getSelectionModel().getSelectedIndex();

					if (selectedIndex < listView.getItems().size() - 1)
						listView.getSelectionModel().select(selectedIndex + 1);

					listView.getItems().remove(selectedIndex);

					try {
						writer = new BufferedWriter(new FileWriter("testsongs.json"));
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
				} else {
					alert.close();
				}

				
			}
		});

		editButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (listView.getItems().isEmpty())
					return;
				
				AlterLibraryDialog dialog = new AlterLibraryDialog(mainStage, "Edit", listView);
			}
		});
	}

	private void showSelectedSongDetails(Song selectedSong) {
		listView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {

			public ListCell<Song> call(ListView<Song> param) {
				final ListCell<Song> cell = new ListCell<Song>() {
					@Override
					public void updateItem(Song item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							if (item.equals(selectedSong)) {
								setText(item.getName() + " - " + item.getArtist() + "\r" + item.getAlbum() + "\t\t"
										+ item.getYear());
							} else
								setText(item.toString());
						}
					}
				};
				return cell;
			}
		});
	}

}
