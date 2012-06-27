package ua.felyne.game.shared.agwtapi;

public enum AGWTapiFactory {
	INSTANCE;
	
	private IController controller = null;
	
	private class UnknownArchApiException extends Exception {
		public UnknownArchApiException() {
			super();
		}
		
		public String toString(){
		    return "UnknownArchApiException";
	   }  
	}
	
	public void create(String type) throws UnknownArchApiException{
		if(type.equals("GWT")) {
			controller = new GWTController();
		}
		else {
			if(type.equals("LWJGL")) {
				controller = new LWJController();
			}
			else {
				 throw new UnknownArchApiException();
			}
		}
	}
	
	public IController getController() throws UnknownArchApiException {
		if(controller != null) {
			return controller;
		}
		else {
			throw new UnknownArchApiException();
		}
	}
}
