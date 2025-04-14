package com.salir.data.mapper

import com.salir.data.remote.dto.CompanyProfileResponse
import com.salir.data.remote.dto.TickerInfoResponse
import com.salir.domain.model.TickerInfo

fun tickerInfoToDomain(
    info: TickerInfoResponse,
    company: CompanyProfileResponse
): TickerInfo = TickerInfo(
    name = company.ticker,
    currentPrice = info.c,
    percentChange = info.dp,
    companyName = company.name,
    logoUrl = company.logo
)