package com.adrscan.ui.component

import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.adrscan.R
import com.adrscan.ui.theme.AccentMain
import com.adrscan.ui.theme.TextPrimary
import com.adrscan.ui.theme.TextSecondary

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val versionName = remember {
        try {
            val pi: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pi.versionName ?: "1.0.1"
        } catch (_: Exception) {
            "1.0.1"
        }
    }

    val repoUrl = "https://github.com/zxs-ai/ADR-Scan"
    val email = "zz177060@gmail.com"

    val annotatedText = buildAnnotatedString {
        withStyle(SpanStyle(color = TextPrimary)) {
            append(context.getString(R.string.about_desc))
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = context.getString(R.string.about_title),
                color = TextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = annotatedText)

                HorizontalDivider(color = TextSecondary.copy(alpha = 0.3f))

                // Project URL
                Text(
                    text = context.getString(R.string.about_project),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = AccentMain)) {
                            append(repoUrl)
                        }
                    },
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repoUrl))
                        context.startActivity(intent)
                    }
                )

                // Feedback email
                Text(
                    text = context.getString(R.string.about_feedback),
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = AccentMain)) {
                            append(email)
                        }
                    },
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$email")
                        }
                        context.startActivity(intent)
                    }
                )

                HorizontalDivider(color = TextSecondary.copy(alpha = 0.3f))

                // Version
                Text(
                    text = "${context.getString(R.string.about_version)}: $versionName",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(context.getString(R.string.confirm))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = TextPrimary,
        textContentColor = TextPrimary
    )
}
