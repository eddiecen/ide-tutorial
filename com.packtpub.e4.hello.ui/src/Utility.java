
public class Utility {
	public static boolean breakpoint() {
		System.out.println("Breakpoint");
		return false;
	}
	
	public static String getOSpaceName(String packageNameStr) {
		String ospaceName = "";

		if (packageNameStr != null && !packageNameStr.isEmpty()) {
			String[] test =packageNameStr.split("::", 1); 
			ospaceName = test[0];
		}
		return ospaceName;
	}
}
