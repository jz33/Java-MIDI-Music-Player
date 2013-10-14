package view;

public interface IModelControlAdapter {
	
	public String load(String songName);
	
	public String parse();
	
	public void play(Object instrument);
	
	public void stop();
	
}
