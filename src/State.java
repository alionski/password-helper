public class State {
	private String master, url, answer;
	private int seed;

	public State(String master, String url, int seed, String answer) {
		this.master = master;
		this.url = url;
		this.seed = seed;
		this.answer = answer;
	}

	public String getMaster() {
		return master;
	}

	public String getUrl() {
		return url;
	}

	public String getAnswer() {
		return answer;
	}

	public int getSeed() {
		return seed;
	}
}
