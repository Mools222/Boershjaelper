package com.example.time18_boershjaelper;

public class Kurvekseler {

    public static double breakeven(double antalAktier, double koebskurs, double kurtage) {
        return (antalAktier * koebskurs + kurtage) / antalAktier;
    }

    public static double tjentEn(double antalAktier, double koebskurs, double kurtage) {
        return ((antalAktier * koebskurs) * 1.01 + kurtage) / antalAktier;
    }

    public static double tjentFem(double antalAktier, double koebskurs, double kurtage) {
        return ((antalAktier * koebskurs) * 1.05 + kurtage) / antalAktier;
    }
}
