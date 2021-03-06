package com.mortgage.server.mortgage.loans.fixeRate

import com.mortgage.server.mortgage.enums.LoanType
import com.mortgage.server.mortgage.enums.RateChangesJumps
import com.mortgage.server.mortgage.loans.TOTAL_PERCENTAGE
import com.mortgage.server.mortgage.loans.YEARLY_NUMBER_OF_MONTHS
import com.mortgage.server.mortgage.loans.oop.AbstractLoan
import com.mortgage.server.mortgage.models.LoanPayment
import kotlin.math.pow

open class FixedRate(principle: Double = 0.0, rate: Double = 0.0, monthsLength: Int = 0, loanType: LoanType = LoanType.FIX_RATE, var monthlyMadad: Double? = null) : AbstractLoan(loanType, principle, rate, monthsLength) {
    override fun downPayment(currentPrinciple: Double, monthsRemains: Int): Double {
        val monthlyRate = currentRate() / YEARLY_NUMBER_OF_MONTHS / TOTAL_PERCENTAGE
        return currentPrinciple * monthlyRate * (1 + monthlyRate).pow(monthsRemains.toDouble()) /
                ((1 + monthlyRate).pow(monthsRemains.toDouble()) - 1)
    }

    protected fun getRateFor(currentPrinciple: Double, paymentNumber: Int): Double {
        prepareForRateChange(paymentNumber)
        return currentPrinciple * (currentRate() / YEARLY_NUMBER_OF_MONTHS) / TOTAL_PERCENTAGE
    }

    override fun calculatesPaymentsFlowChart(limit: Int) : List<LoanPayment> {
        val downPayment = downPayment(principle, monthsLength)
        val result = ArrayList<LoanPayment>()
        for (index in 0 until limit) {
            val monthInitialPrinciple = if (result.isEmpty()) principle else result[index -1].afterPaymentPrinciple
            val ratePayment = getRateFor(monthInitialPrinciple, index + 1)
            val loanPayment = LoanPayment(monthInitialPrinciple, ratePayment, downPayment, 0.0)
            result.add(loanPayment)
        }
        return result

    }

    override fun calculatesPrincipleChanges(monthlyPrinciple: Double) : Double {
        monthlyMadad?.let {it
            return monthlyPrinciple * (1 + (it/ TOTAL_PERCENTAGE))
        }. run {
            return monthlyPrinciple
        }
    }

    override fun prepareForRateChange(paymentNumber: Int) {
        //DO noting
    }

    override fun rateChangesJump(): RateChangesJumps {
        return RateChangesJumps.NONE
    }
}