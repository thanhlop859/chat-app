package com.chatApp.sp.utils;

import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		int soDu = 50500;
		int count = 1;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Nhap so tien can rut: ");
		int rut = sc.nextInt();
		sc.nextLine();
		
		while(soDu < rut || rut < 0) {
			System.out.println("So tien ko hop le, tai khoan con: "+soDu);
			System.out.println("Nhap lai so tien: ");
			try {
				int s = sc.nextInt();
				System.out.println(s);
				//rut = Integer.parseInt(s);
			}catch(Exception e) {
				if(count == 3) {
					System.out.println("ket thuc thao tac!");
					break;
				}else System.out.println("Khong hop le, con "+(3-count) +" thu lai");
			}
			count++;
		}

	}

}
