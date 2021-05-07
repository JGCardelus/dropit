package debug;

public class DebugTools {
	public static final boolean DEBUG = true;

	public static void print(String message) {
		if (DEBUG) {
			System.out.println(message);
		}
	}
}
