package kr.prev.ndnd.controller;

import android.app.Activity;
import android.widget.TextView;
import kr.prev.ndnd.R;
import kr.prev.ndnd.data.SummarayData;
import kr.prev.ndnd.utils.NdUtil;

public class SummaryDataViewController {
	public SummarayData summaryData;

    private Activity activity;
	private TextView lendText;
    private TextView loanText;

	public SummaryDataViewController(Activity activity) {
		this.activity = activity;
        this.lendText = (TextView) activity.findViewById(R.id.sumLendText);
        this.loanText = (TextView) activity.findViewById(R.id.sumLoanText);
	}

	public void update() {
        lendText.setText(NdUtil.getFormattedAmount(summaryData.lend));
        loanText.setText(NdUtil.getFormattedAmount(summaryData.loan));
	}
}
