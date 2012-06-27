package ua.felyne.game.client;

public class Util {
	public static native void log(Object msg) /*-{
		$wnd.console.log(msg);
	}-*/;
	
	public static void log(int num) {
		log(String.valueOf(num));
	}
	
	public static native void warn(Object msg) /*-{
		$wnd.console.warn(msg);
	}-*/;
	
	public static void warn(int num) {
		warn(String.valueOf(num));
	}
	
	public static native void enhancedLinks() /*-{
		new $wnd.Scrollmenu('menu');
	}-*/;
	
	public static native void showGame() /*-{
		$wnd.j("#show").click()	
	}-*/;

}
