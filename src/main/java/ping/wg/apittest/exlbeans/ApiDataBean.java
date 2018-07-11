package ping.wg.apittest.exlbeans;

public class ApiDataBean extends BaseBean {
	private boolean run;
	private String desc; // 接口描述
	private String url;
	private String method;
	private String param;
	private String verify;
	private String save;

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getSave() {
		return save;
	}

	public void setSave(String save) {
		this.save = save;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("desc:%s,method:%s,url:%s,param:%s", this.desc,
				this.method, this.url, this.param);
	}

}
