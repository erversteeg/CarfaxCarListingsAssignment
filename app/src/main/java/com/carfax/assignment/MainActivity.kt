package com.carfax.assignment

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.carfax.assignment.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val callPermissionRequestCode = 1000

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            showCallPhoneNotGrantedDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        checkPhonePermission()
    }

    private fun checkPhonePermission() {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CALL_PHONE) -> {
                showCallPhonePermissionRationale()
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            callPermissionRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showCallPhoneNotGrantedDialog()
                }
            }
            else -> {

            }
        }
    }

    private fun showCallPhonePermissionRationale() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(getString(R.string.call_phone_permission_msg))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.call_phone_permission_positive_button),
            ({ dialog, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.CALL_PHONE)

                dialog.dismiss()
            })
        )
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.call_phone_permission_negative_button),
            ({ dialog, _ ->
                dialog.dismiss()
            })
        )

        alertDialog.show()
    }

    private fun showCallPhoneNotGrantedDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage(getString(R.string.call_phone_permission_not_granted_msg))
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.call_phone_permission_not_granted_negative_button),
            ({ dialog, _ ->
                dialog.dismiss()
            })
        )
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun expandAppBarLayout() {
        val appBarLayout = binding.appBarLayout
        appBarLayout.setExpanded(true)
    }
}