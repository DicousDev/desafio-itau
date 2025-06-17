package br.com.desafioitau.order.model;

import java.math.BigDecimal;

public class CustomerNoInformation implements CustomerPolicyAnalyse {

    @Override
    public PolicySolicitation analyse(Policy policy) {
        PolicySolicitation result = PolicySolicitation.REJECTED;
        if((InsuranceCategory.LIFE.equals(policy.getCategory()) ||
            InsuranceCategory.RESIDENTIAL.equals(policy.getCategory()))) {

            if(BigDecimal.valueOf(20000).compareTo(policy.getInsuredAmount()) >= 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(InsuranceCategory.AUTO.equals(policy.getCategory())) {

            if(BigDecimal.valueOf(75000).compareTo(policy.getInsuredAmount()) >= 0) {
                result = PolicySolicitation.PENDING;
            }
        }
        else if(BigDecimal.valueOf(55000).compareTo(policy.getInsuredAmount()) >= 0) {
            result = PolicySolicitation.PENDING;
        }

        return result;
    }

    public static void main(String[] args) {
        var x = BigDecimal.valueOf(20000).compareTo(BigDecimal.valueOf(30000));
        System.out.println(x);
    }
}
