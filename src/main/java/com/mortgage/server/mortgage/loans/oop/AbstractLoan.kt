package com.mortgage.server.mortgage.loans.oop

import com.mortgage.server.mortgage.enums.LoanType
import com.mortgage.server.mortgage.enums.RateChangesJumps
import com.mortgage.server.mortgage.models.LoanPayment
import com.mortgage.server.mortgage.models.LoanSummary

abstract class AbstractLoan(var loanType: LoanType, var principle: Double = 0.0,  var initialRate: Double = 0.0, var monthsLength: Int = 0) : Cloneable {
    abstract fun downPayment(currentPrinciple: Double, monthsRemains: Int): Double
    abstract fun calculatesPaymentsFlowChart(limit: Int = monthsLength) : List<LoanPayment>
    abstract fun calculatesPrincipleChanges(monthlyPrinciple: Double) : Double
    abstract fun prepareForRateChange(paymentNumber: Int)
    abstract fun rateChangesJump() : RateChangesJumps
    var rates = ArrayList<Double>()
        private set

    init {
        this.rates.add(initialRate)
    }

    public override fun clone(): AbstractLoan {
        return super.clone() as AbstractLoan
    }

    fun overrideRate(initialRate: Double) {
        this.initialRate = initialRate
        rates.clear()
        rates.add(initialRate)
    }

    fun currentRate() : Double {
        return rates.last()
    }

    fun getFinancingPercentage(totalLoanAmount: Double) : Double {
        return totalLoanAmount / principle
    }

    fun getFirstPayment() : Double {
        val value = calculatesPaymentsFlowChart(1).firstOrNull()?.downPayment
        return value ?: 0.0
    }

    fun calculateLoanSummaryFor(requiredPrinciple: Double) : LoanSummary {
        var downPaymentSum = 0.0
        this.calculatesPaymentsFlowChart().forEach {  loanPayment ->
            downPaymentSum += loanPayment.downPayment
        }
        val loanFirstPayment: Double = this.calculatesPaymentsFlowChart(1).sumByDouble { it.downPayment }
        return LoanSummary((principle/requiredPrinciple), loanType, principle, initialRate, monthsLength, loanFirstPayment, downPaymentSum)
    }
}