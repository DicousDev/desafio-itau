package br.com.desafioitau.order.model;

public enum CustomerType {

    REGULAR(new CustomerRegular()),
    HIGH_RISK(new CustomerHighRisk()),
    PREFERENTIAL(new CustomerPreferential()),
    NO_INFORMATION(new CustomerNoInformation());

    CustomerPolicyAnalyse customerPolicyAnalyse;

    CustomerType(CustomerPolicyAnalyse customerPolicyAnalyse) {
        this.customerPolicyAnalyse = customerPolicyAnalyse;
    }

    public PolicySolicitation analysePolicy(Policy policy) {
        return customerPolicyAnalyse.analyse(policy);
    }
}
