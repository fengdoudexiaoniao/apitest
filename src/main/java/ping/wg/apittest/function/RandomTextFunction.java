package ping.wg.apittest.function;
import ping.wg.apittest.utils.RandomUtil;
import ping.wg.apittest.utils.StringUtil;

public class RandomTextFunction implements Function {

	@Override
	public String execute(String[] args) {
		int length = 6;// 默认为6
		if (StringUtil.isNotEmpty(args[0])) {
			length = Integer.valueOf(args[0]);// 参数是长度
		}
		return RandomUtil.getRandomText(length);
	}

	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "randomText";
	}

}
