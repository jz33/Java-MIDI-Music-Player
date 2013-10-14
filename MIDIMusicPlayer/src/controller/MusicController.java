package controller;

import provided.util.ABCInstrument;
import view.IModelControlAdapter;
import view.MusicGUI;
import model.IViewControlAdapter;
import model.MusicModel;

public class MusicController {

	MusicGUI<ABCInstrument> _view;
    MusicModel _model;
     
	public static void main(String[] args){
		new MusicController().run();
	}

	private void run(){
		this._model.start();
		this._view.start();
	}

	public MusicController(){
		_view=new MusicGUI<ABCInstrument>(new IModelControlAdapter() {
			
			public String load(String songName) {
				return MusicController.this._model.load(songName);
			}
			
			public String parse() {
				return MusicController.this._model.parse();
			}
			
			public void play(Object instrument) {
				MusicController.this._model.play(instrument);
			}

			public void stop() {
				MusicController.this._model.stop();
			}
		});
		
		_model=new MusicModel(new IViewControlAdapter() {
			
			public void addInstrument(ABCInstrument instrument) {
				MusicController.this._view.addInstrument(instrument);
			}

			public void addMusicFiles(String filename) {
				MusicController.this._view.addMusicFiles(filename);
			}
		});
	}
}
