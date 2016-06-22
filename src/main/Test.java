package main;

import reality_mining.user.ProviderFilter;

public class Test {
	public static void main(String[] args) {
		System.out.println(MatlabHelpers.serialDateToDate(Double.valueOf("732324.728148148")));
		System.out.println(MatlabHelpers.serialDateToUnixtime(Double.valueOf("732324.728148148")));
		
		System.out.println(ProviderFilter.removeProviderFromStart("AT&TWirelGreen"));
	}
}
