package a2;

@SuppressWarnings("serial")
public class NullKeyboardException extends Exception {
	String kbName;
	public NullKeyboardException(String kbName) {
		this.kbName = kbName;
	}
	public String getKbName() {
		return kbName;
	}
	@Override
	public void printStackTrace() {
		System.out.println("kbName: " + kbName);
		super.printStackTrace();
	}
}
