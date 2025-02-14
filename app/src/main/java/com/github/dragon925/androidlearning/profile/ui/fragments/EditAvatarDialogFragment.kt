package com.github.dragon925.androidlearning.profile.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.github.dragon925.androidlearning.databinding.DialogEditAvatarBinding

class EditAvatarDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "EditAvatarDialogFragment"
        const val REQUEST_KEY = "EditAvatarDialogFragment_Request"
        const val RESULT_TYPE = "EditAvatarDialogFragment_Result"

        const val RESULT_CODE_TAKE_PHOTO = 1
        const val RESULT_CODE_CHOOSE_PHOTO = 2
        const val RESULT_CODE_DELETE_PHOTO = 3
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val dialogBinding = DialogEditAvatarBinding.inflate(requireActivity().layoutInflater)
                .apply {
                btnChoosePhoto.setOnClickListener { choosePhoto() }
                btnTakePhoto.setOnClickListener { takePhoto() }
                btnDeletePhoto.setOnClickListener { deletePhoto() }
            }
            val builder = AlertDialog.Builder(it)
            builder.setView(dialogBinding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun choosePhoto() {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT_TYPE to RESULT_CODE_CHOOSE_PHOTO)
        )
        dismiss()
    }

    private fun takePhoto() {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT_TYPE to RESULT_CODE_TAKE_PHOTO)
        )
        dismiss()
    }

    private fun deletePhoto() {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(RESULT_TYPE to RESULT_CODE_DELETE_PHOTO)
        )
        dismiss()
    }
}