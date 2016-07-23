package kr.prev.ndnd.controller;

import android.app.Activity;
import android.widget.TextView;

import java.util.List;

import kr.prev.ndnd.R;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.data.SummarayData;
import kr.prev.ndnd.utils.FormatUtil;

public class SummaryDataViewController implements IViewController {
	SummarayData summaryData;

	Activity activity;
	TextView lendText;
	TextView loanText;

	public SummaryDataViewController(Activity activity) {
		this.activity = activity;
		this.lendText = (TextView) activity.findViewById(R.id.sumLendText);
		this.loanText = (TextView) activity.findViewById(R.id.sumLoanText);
	}

	public void setData(SummarayData data) { this.summaryData = data; }
	public SummarayData getData() { return this.summaryData; }

	@Override
	public void update() {
		lendText.setText(FormatUtil.getFormattedAmount(summaryData.lend));
		loanText.setText(FormatUtil.getFormattedAmount(summaryData.loan));
	}

	public void renewData(List<RecordData> recordDatas) {
		int sumLend = 0;
		int sumLoan = 0;
		for (RecordData rd : recordDatas) {
			if (rd.state == 1) continue;

			if (rd.type == 0) sumLend += rd.amount;
			else sumLoan += rd.amount;
		}

		this.summaryData.lend = sumLend;
		this.summaryData.loan = sumLoan;
	}
}
