package io.card.payment;


import android.app.Activity;

import java.lang.reflect.Method;

import io.card.payment.CardScanner;

public class CardScannerWrapper {

    public static void initCardScanner() {

        try {
            /*Method m1 = department.getClass().getMethod("getEmployees");
            Employee[] employees = (Employee[]) m1.invoke(department);

            for (Employee employee : employees) {
                Method m2 = employee.getClass().getMethod("getAddress");
                Address address = (Address) m2.invoke(employee);

                Method m3 = address.getClass().getMethod("getStreet");
                Street street = (Street) m3.invoke(address);

                System.out.println(street);
            }*/
        }

// There are many checked exceptions that you are likely to ignore anyway
        catch (Exception ignore) {


    /* public static void initCardScanner() {

         Reflect.compile(
                "io.card.payment.CardIOActivity",
                """
                                 public static void aaa() {
                                      System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");


                                 }
                        """
        ).create().get();
    }*/
        }
    }
}
