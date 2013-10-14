package model;

import provided.abcParser.ABCParser;
import provided.music.APhraseVisitor;
import provided.music.Chord;
import provided.music.Header;
import provided.music.IPhrase;
import provided.music.IPhraseVisitor;
import provided.music.IPhraseVisitorCmd;
import provided.music.NESeqList;
import provided.music.Note;
import provided.music.Triplet;
import provided.player.ISequencePlayerStatus;
import provided.player.SequencePlayer;
import provided.util.ABCInstrument;
import provided.util.ABCUtil;
import provided.util.KeySignature;

public class MusicModel {

	private SequencePlayer _sp;
	private ABCParser _parser;
	private IPhrase _music;
	private IPhraseVisitor _player;
	private ABCUtil _abcUtil = ABCUtil.Singleton;
	private ABCInstrument[] _instruments;
	private IViewControlAdapter _view;
	
	private final String[] files ={
			"beethoven_fav_waltz.abc",
			"fur_elise.abc",
			"happy_birthday.abc",
			"joy_to_the_world.abc",
			"little_night_music.abc",
			"ode_to_joy.abc",
			"paddy.abc",
			"prelude.abc",
			"scale.abc",
			"smile.abc",
			"star_spangled.abc",
			"twinkle.abc"
	}; 
	
	public MusicModel(IViewControlAdapter view){
		this._view = view;
		this._instruments = _abcUtil.getInstruments();
		this.Init_SequencePlayer(0);
	}
	
	public void start(){
		
		for(ABCInstrument instr : _instruments){
			_view.addInstrument(instr);
		}
		
		for(String f : files){
			_view.addMusicFiles(f);
		}
				
        NESeqList.setToStringAlgo(new APhraseVisitor() {
        	
        	APhraseVisitor _helper = this; 
        	{
 
        		addCmd("MTSeqList", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {						
						return params[0]+"}";
					}
				});

        		addCmd("NESeqList", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {
						System.out.println("NECase: host.first = "+((NESeqList) host).getFirst());
						return ((NESeqList) host).getRest().execute(_helper, (String)params[0] + ", " +((NESeqList) host).getFirst().toString());
					}
				});
        		
        	}
		});	
        
        this._player = new APhraseVisitor(new IPhraseVisitorCmd(){

        	/**
        	 * 0. Unknown host
        	 */
			public Object apply(String id, IPhrase host, Object... params) {
				return null;
			}
        	
        }){
        	{        		
        		/**
        		 * 1. Empty list
        		 */
        		addCmd("MTSeqList", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {
						return (int)params[0];
					}
				});
        		
        		/**
        		 * 2. Non empty list
        		 */
        		addCmd("NESeqList", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {
						int curTick = (Integer) params[0];
						NESeqList list = (NESeqList) host;
						curTick = (Integer) list.getFirst().execute(_player, curTick);
						curTick = (Integer) list.getRest().execute(_player, curTick);
						return curTick;
					}
				});
        		
        		/**
        		 * 3. Default Note
        		 */
        		addCmd("L", new IPhraseVisitorCmd() {
        							
        		    public Object apply(String id, IPhrase host, Object... params) {					
        		    	int curTick = (Integer) params[0];
						Header h = (Header) host;
						double note = _abcUtil.parseFraction(h.getValue());
						int ticksPerQuarterNote = _sp.getTicksPerQuarterNote();
						int defaultNote = (int) ((note / 0.25) * ticksPerQuarterNote);
						_sp.setTicksPerDefaultNote(defaultNote);
						return curTick;
        			}
        		});
        		
        		/**
        		 * 4. Key signatures
        		 */
        		addCmd("K", new IPhraseVisitorCmd() {

        			public Object apply(String id, IPhrase host, Object... params) {
        				int curTick = (Integer) params[0];
						Header h = (Header) host;
						final KeySignature ksig = new KeySignature(h.getValue());
						addCmd("Note", new IPhraseVisitorCmd() {
							
							public Object apply(String id, IPhrase host, Object... params) {
								int curTick = (Integer) params[0];
								Note note = ksig.adjust((Note) host);
								return _sp.addNote(note, curTick);
							}
							
						});
						return curTick;
        		    }
        		});
        		
        		
        		/**
        		 * 5. Tempo
        		 */
        		addCmd("Q", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {
						int curTick = (Integer) params[0];
						Header h = (Header) host;
						int ticksPerQuarterNote = _sp.getTicksPerQuarterNote();
						int ticksPerDefaultNote = _sp.getTicksPerDefaultNote();
						double defaultNotesPerQuarterNote = ((double) ticksPerQuarterNote) / ((double) ticksPerDefaultNote);
						double bpm = _abcUtil.parseTempo(h.getValue(), defaultNotesPerQuarterNote);
						_sp.setTempo((int) bpm);
						return curTick;
					}
				});
				
        		
        		/**
        		 * 6. Chord
        		 */
        		addCmd("Chord", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {
						int curTick = (Integer) params[0];
						Chord chord = (Chord) host;
						int endTick = curTick;
						for (Note n: chord.getNotes()) {
							endTick = (Integer) n.execute(_player, curTick);
						}
						return endTick;
					}
				});
        		
        		/**
        		 * 7. Triplet
        		 */
        		addCmd("Triplet", new IPhraseVisitorCmd() {
					
					public Object apply(String id, IPhrase host, Object... params) {
						int curTick = (Integer) params[0];
						Triplet triplet = (Triplet) host;
						for (Note n: triplet.getNotes()) {
							double duration = n.getDuration();
							Note t = new Note(n.getName(), n.getOctave(), n.getAccidental(), duration * 2.0 / 3.0, n.getIsNatural());
							curTick = (Integer) t.execute(_player, curTick);
						}
						return curTick;
					}
				});
        		
        		/**
        		 * 8. Individual notes
        		 */
        		addCmd("Tuplet", new IPhraseVisitorCmd(){

					public Object apply(String id, IPhrase host,
							Object... params) {
						// TODO Auto-generated method stub
						return null;
					}
        			
        		});
        		
        		/**
        		 * 9. Ignored headers
        		 */
        		String headerString = "ABCDEFGHIJMNOPRSTUVWXYZ";
        		for (int i = 0; i < headerString.length(); i++) {
        		     addCmd("" + headerString.charAt(i), new IPhraseVisitorCmd() {
   
						public Object apply(String id, IPhrase host, Object... params) {
							int curTick = (Integer) params[0];
							return curTick;
						}
					});
        		     
        		}
			 }
        };
	}

	private void Init_SequencePlayer(int instrumentID) {
		if (_sp == null) {
			_sp = new SequencePlayer(16, instrumentID);
		} else {
			_sp.init(16, instrumentID);
		}
		_sp.setTicksPerDefaultNote(16);
		_sp.setTempo(125);
	}

	public String load(String songName){
	    String filename = "/songs/" + songName;
        this._parser = new ABCParser(filename);
		return _abcUtil.getFileContents(filename);	
	}

	public String parse(){
	    if (_parser == null) return "No parser.";
        _music = _parser.parse();  

        return (String)_music.execute(new APhraseVisitor(new IPhraseVisitorCmd(){

        	public Object apply(String id, IPhrase host, Object... params) {
				return host.toString();
			}
        	
        }){
        	
        	{
    			addCmd("NESeqList", new IPhraseVisitorCmd(){

    				public Object apply(String id, IPhrase host, Object... params) {
    					NESeqList neHost = (NESeqList) host;
    					return  ((NESeqList) host).getFirst() + "{" +
    							(String)neHost.getRest().execute(helper, "{");
    				}
    				
    			});
    			
    			addCmd("MTSeqList", new IPhraseVisitorCmd(){

    				public Object apply(String id, IPhrase host, Object... params) {
    					return "{}";
    				}
    				
    			});
    		}
        });
	}
	
	private IPhraseVisitor helper = new APhraseVisitor(new IPhraseVisitorCmd(){
		
		public Object apply(String id, IPhrase host, Object... params) {
			return host.toString();
		}
	})
	{	
		{
			addCmd("NESeqList", new IPhraseVisitorCmd(){

				public Object apply(String id, IPhrase host, Object... params) {
					NESeqList neHost = (NESeqList) host;
					return  ((NESeqList) host).getFirst()+(String)neHost.getRest().execute(helper, ((String)params[0]));
				}
				
			});
			
			addCmd("MTSeqList", new IPhraseVisitorCmd(){

				public Object apply(String id, IPhrase host, Object... params) {
					return params[0]+"}";
				}
				
			});
		}		
	};
	
	public void play(Object instrument){
	    Init_SequencePlayer(((ABCInstrument)instrument).getValue());
	    _music.execute(this._player, 0);
		_sp.play(ISequencePlayerStatus.NULL);
	}
	
	public void stop(){
		_sp.stop();
	}
}
