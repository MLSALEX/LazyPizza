package com.alexmls.lazypizza.core.designsystem.theme

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.alexmls.lazypizza.R

val Icons.Filled.Remove: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_minus)

val Icons.Filled.Trash: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_trash)

val Icons.Filled.Search: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_search)

val Icons.Outlined.Person: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.person)

val Icons.Outlined.Logout: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logout)