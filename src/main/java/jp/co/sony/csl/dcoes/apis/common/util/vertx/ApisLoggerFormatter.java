package jp.co.sony.csl.dcoes.apis.common.util.vertx;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Sets dedicated log format for APIS.
 * The program identification string is output.
 * Parsed by {@link jp.co.sony.csl.dcoes.apis.tools.log.util.ApisVertxLogParser}.
 * @author OES Project
 * APIS 専用のログフォーマット.
 * プログラム識別文字列が出力される.
 * {@link jp.co.sony.csl.dcoes.apis.tools.log.util.ApisVertxLogParser} でパースされる.
 * @author OES Project
 */
public class ApisLoggerFormatter extends Formatter {

	private String PROGRAM_ID_ = null;

	/**
	 * Adds {@link #programId() program identification string} to standard format output.
	 * @param record {@inheritDoc}
	 * @return {@inheritDoc}
	 * 標準フォーマットに {@link #programId() プログラム識別文字列} を追加して出力する.
	 * @param record {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override public String format(final LogRecord record) {
		String result = formatRecord(record);
		return "[[[" + programId() + "]]] " + result;
	}

	/**
	 * Formats a LogRecord into a string.
	 * Uses default Java logging format with timestamp, level, logger name, and message.
	 * @param record {@inheritDoc}
	 * @return formatted log string
	 */
	private String formatRecord(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		sb.append(record.getMillis()).append(' ');
		sb.append(record.getLevel().getName()).append(' ');
		sb.append(record.getLoggerName()).append(" : ");
		sb.append(record.getMessage());
		if (record.getThrown() != null) {
			sb.append("\n").append(getStackTrace(record.getThrown()));
		}
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * Gets stack trace string from Throwable.
	 * @param t throwable
	 * @return stack trace string
	 */
	private String getStackTrace(Throwable t) {
		java.io.StringWriter sw = new java.io.StringWriter();
		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * Returns program identification string, which is output to a log.
	 * Returns emptry string if {@code null}.
	 * @return program identification string
	 * ログに出力するプログラム識別文字列を返す.
	 * {@code null} なら空文字列を返す.
	 * @return プログラム識別文字列
	 */
	protected String programId() {
		if (PROGRAM_ID_ == null) PROGRAM_ID_ = VertxConfig.programId();
		return (PROGRAM_ID_ != null) ? PROGRAM_ID_ : "";
	}

}
