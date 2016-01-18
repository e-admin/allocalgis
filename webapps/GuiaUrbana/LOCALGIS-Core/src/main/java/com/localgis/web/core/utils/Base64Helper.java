/**
 * Base64Helper.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.utils;

public class Base64Helper {
	private Base64Helper()
	{
	}

	public static final Base64Helper getInstance()
	{
		return INSTANCE;
	}

	private byte[] encode3to4(byte abyte0[], int i)
	{
		byte abyte1[] = new byte[4];
		encode3to4(abyte0, 0, i, abyte1, 0);
		return abyte1;
	}

	private byte[] encode3to4(byte abyte0[], int i, int j, byte abyte1[], int k)
	{
		int l = (j > 0 ? (abyte0[i] << 24) >>> 8 : 0) | (j > 1 ? (abyte0[i + 1] << 24) >>> 16 : 0) | (j > 2 ? (abyte0[i + 2] << 24) >>> 24 : 0);
		switch(j)
		{
		case 3: // '\003'
		abyte1[k] = ALPHABET[l >>> 18];
		abyte1[k + 1] = ALPHABET[l >>> 12 & 0x3f];
		abyte1[k + 2] = ALPHABET[l >>> 6 & 0x3f];
		abyte1[k + 3] = ALPHABET[l & 0x3f];
		return abyte1;

		case 2: // '\002'
			abyte1[k] = ALPHABET[l >>> 18];
			abyte1[k + 1] = ALPHABET[l >>> 12 & 0x3f];
			abyte1[k + 2] = ALPHABET[l >>> 6 & 0x3f];
			abyte1[k + 3] = 61;
			return abyte1;

		case 1: // '\001'
			abyte1[k] = ALPHABET[l >>> 18];
			abyte1[k + 1] = ALPHABET[l >>> 12 & 0x3f];
			abyte1[k + 2] = 61;
			abyte1[k + 3] = 61;
			return abyte1;
		}
		return abyte1;
	}

	public String encodeBytes(byte abyte0[])
	{
		return encodeBytes(abyte0, abyte0.length);
	}

	public String encodeBytes(byte abyte0[], int i)
	{
		int j = (i * 4) / 3;
		byte abyte1[] = new byte[j + (i % 3 > 0 ? 4 : 0) + j / 76];
		int k = 0;
		int l = 0;
		int i1 = i - 2;
		int j1 = 0;
		while(k < i1) 
		{
			encode3to4(abyte0, k, 3, abyte1, l);
			if((j1 += 4) == 76)
			{
				abyte1[l + 4] = 10;
				l++;
				j1 = 0;
			}
			k += 3;
			l += 4;
		}
		if(k < i)
		{
			encode3to4(abyte0, k, i - k, abyte1, l);
			l += 4;
		}
		return new String(abyte1, 0, l);
	}

	public String encodeString(String s)
	{
		return encodeBytes(s.getBytes());
	}

	private static final Base64Helper INSTANCE = new Base64Helper();
	public static final boolean ENCODE = true;
	public static final boolean DECODE = false;
	private static final byte ALPHABET[] = {
		65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
		75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
		85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
		101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
		111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
		121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
		56, 57, 43, 47
	};
	private static final byte DECODABET[] = {
		-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
		-5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
		-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
		-9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
		-9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 
		54, 55, 56, 57, 58, 59, 60, 61, -9, -9, 
		-9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 
		5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
		15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
		25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 
		29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
		39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
		49, 50, 51, -9, -9, -9, -9
	};
}
