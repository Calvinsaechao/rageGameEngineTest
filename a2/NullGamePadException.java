package a2;

@SuppressWarnings("serial")
public class NullGamePadException extends Exception {
	String gpName;
	public NullGamePadException(String gpName) {
		this.gpName = gpName;
	}
	public String getGpName() {
		return gpName;
	}
	@Override
	public void printStackTrace() {
		System.out.println("gpName: " + gpName);
		super.printStackTrace();
	}
}
