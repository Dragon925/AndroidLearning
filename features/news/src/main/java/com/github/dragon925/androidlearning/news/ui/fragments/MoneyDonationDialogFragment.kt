package com.github.dragon925.androidlearning.news.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme

class MoneyDonationDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "MoneyDonationDialogFragment"
        const val REQUEST_KEY = "MoneyDonationDialogFragment_Request"
        const val RESULT_VALUE = "MoneyDonationDialogFragment_Result"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext()).apply {
            window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
            setContentView(
                ComposeView(context).apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        AppTheme {
                            MoneyDonationDialog(
                                modifier = Modifier.fillMaxWidth(),
                                onDismiss = ::dismiss,
                                onDonate = ::setDonationResult
                            )
                        }
                    }
                }
            )
        }

    }

    private fun setDonationResult(amount: Int) {
        setFragmentResult(REQUEST_KEY, bundleOf(RESULT_VALUE to amount))
        dismiss()
    }

}