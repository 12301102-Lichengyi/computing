package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Numeral {
	public static void main(String[] args) {
		System.out.println(gcd(18, 30));
	}

	public static int digitsInNumber(long num){
		int cur = 0;
		while(num > 0){
			num /= 10;
			cur++;
		}
		return cur;
	}
	
	public static Set<Long> divisors(long l){
		Set<Long> ret = new HashSet<Long>();
		for(long i=1; i*i<=l; i++){
			if(l%i==0){
				ret.add(i);
				ret.add(l/i);
			}
		}
		return ret;
	}
	
	public static long gcd(long a, long b){
		if(b == 0) return a;
		return gcd(b, a%b);
	}
	
	public static class Fraction implements Comparable<Fraction>{
		public final BigInteger numerator;
		public final BigInteger denominator;
        public Fraction(long n){
            this(BigInteger.valueOf(n), BigInteger.ONE);
        }
		public Fraction(long n, long d){
            this(BigInteger.valueOf(n), BigInteger.valueOf(d));
		}
        public Fraction(BigInteger n, BigInteger d){
            if(d.equals(BigInteger.ZERO)) throw new RuntimeException("Divide by zero");
            boolean neg = false;
            if(d.compareTo(BigInteger.ZERO) < 0){
                n = n.negate();
                d = d.negate();
            }
            if(n.compareTo(BigInteger.ZERO) < 0){
                neg = true;
                n = n.negate();
            }
            BigInteger gcd = n.gcd(d);
            n = n.divide(gcd);
			numerator = neg ? n.negate() : n;
            denominator = d.divide(gcd);
        }
		public String toString(){
			return numerator.toString() + (denominator.equals(BigInteger.ONE) ? "" : "/" + denominator.toString());
		}
		public double doubleApprox(int precision){
			return new BigDecimal(numerator).divide(new BigDecimal(denominator), precision, RoundingMode.DOWN).doubleValue();
		}
		public boolean equals(Object obj){
			if(obj instanceof Fraction){
				Fraction other = (Fraction) obj;
				return other.numerator.equals(this.numerator) && other.denominator.equals(this.denominator);
			}
			return false;
		}
		
		public Fraction multiply(Fraction other){
			return new Fraction(this.numerator.multiply(other.numerator), this.denominator.multiply(other.denominator));
		}

		public Fraction add(Fraction other){
            return new Fraction(this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator)), this.denominator.multiply(other.denominator));
        }

		public Fraction subtract(Fraction other){
            return new Fraction(this.numerator.multiply(other.denominator).subtract(other.numerator.multiply(this.denominator)), this.denominator.multiply(other.denominator));
        }

		@Override
		public int compareTo(Fraction o) {
			return this.numerator.multiply(o.denominator).compareTo(o.numerator.multiply(this.denominator));
		}
	}

    //Roman Numerals
	private final static TreeMap<Integer, String> romanIToS;
    private final static List<Integer> romanIOrder;
	static {
        romanIToS = new TreeMap<>();
		romanIToS.put(1000, "M");
		romanIToS.put(900, "CM");
		romanIToS.put(500, "D");
		romanIToS.put(400, "CD");
		romanIToS.put(100, "C");
		romanIToS.put(90, "XC");
		romanIToS.put(50, "L");
		romanIToS.put(40, "XL");
		romanIToS.put(10, "X");
		romanIToS.put(9, "IX");
		romanIToS.put(5, "V");
		romanIToS.put(4, "IV");
		romanIToS.put(1, "I");
        romanIOrder = romanIToS.keySet().stream().sorted().collect(Collectors.toList());
	}

    /**
     * Converts integers to roman numerals
     * (Highest symbol is M=1000)
     */
	public final static String toRoman(int number) {
		int l =  romanIToS.floorKey(number);
		if ( number == l ) {
			return romanIToS.get(number);
		}
		return romanIToS.get(l) + toRoman(number-l);
	}

    /**
     * Converts roman numerals to integers
     * (Highest symbol is M=1000)
     */
    public static int fromRoman(String s){
        int ret = 0;
        for(int rdigit : romanIOrder){
            String rnumer = romanIToS.get(rdigit);
            while(s.endsWith(rnumer)){
                ret += rdigit;
                s = s.substring(0, s.length()-rnumer.length());
            }
        }
        if(s.length() != 0) throw new RuntimeException("Invalid roman numeral " + s);
        return ret;
    }

}
