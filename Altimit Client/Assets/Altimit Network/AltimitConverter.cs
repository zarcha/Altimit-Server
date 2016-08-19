using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System;
using System.Text;

namespace Altimit{
	public class AltimitConverter : MonoBehaviour {
		
		//<summary>
		// Creates a byte array of the message that will be sent.
		//The first 4 bytes is the end size of the whole message.
		//The last 4 bytes is the message key used to identify the end of a message and later to be used for a security purpose.
		//</summary>
		public static byte[] SendConversion(string methodName, params object[] args){
			List<byte[]> byteList = new List<byte[]> ();

			byte[] byteArray = null;
			if (methodName != null || methodName != "") {
				byte[] method = convertToByteArray (methodName);
				byteList.Add (method);

				int size = method.Length;
				string type = "";

				foreach(object paramater in args){
					type = paramater.GetType ().Name;
					byte[] currentArr = null;

					switch (type) {
					case "Char":
						char castChar = (char)paramater;
						currentArr = convertToByteArray (castChar);
						break;
					case "Int32":
						int castInt = (int)paramater;
						currentArr = convertToByteArray (castInt);
						break;
					case "Int64":
						long castLong = (long)paramater;
						currentArr = convertToByteArray (castLong);
						break;
					case "Int16":
						short castShort = (short)paramater;
						currentArr = convertToByteArray (castShort);
						break;
					case "Single":
						float castFloat = (float)paramater;
						currentArr = convertToByteArray (castFloat);
						break;
					case "Double":
						double castDouble = (double)paramater;
						currentArr = convertToByteArray (castDouble);
						break;
					case "Boolean":
						bool castBool = (bool)paramater;
						currentArr = convertToByteArray (castBool);
						break;
					case "String":
						string castString = (string)paramater;
						currentArr = convertToByteArray (castString);
						break;
					case "Guid":
						Guid castUUID = (Guid)paramater;
						currentArr = convertToByteArray (castUUID);
						break;
					default:
						Debug.LogWarning ("Type of " + type + " is not supported for conversion");
						break;
					}

					size += currentArr.Length;
					byteList.Add (currentArr);
				}

				byte[] key = {5, 9, 0, 4 };
				size += 8;

				byte[] temp = new byte[4];
				Array.ConstrainedCopy (convertToByteArray (size), 1, temp, 0, temp.Length);

				byteList.Insert(0, temp);
				byteList.Add (key);


				byteArray = new byte[size];
				int currentIndex = 0;

				foreach (byte[] byteArr in byteList) {
					Array.ConstrainedCopy (byteArr, 0, byteArray, currentIndex, byteArr.Length);
					currentIndex += byteArr.Length;
				}
			}

			return byteArray;
		}

		//<summary>
		// Converts Char into byte array.
		//</summary>
		private static byte[] convertToByteArray(char value){
			byte[] buffer = new byte[3];
			int position = 0;

			buffer [position] = (byte)1;
			position++;

			byte[] val = BitConverter.GetBytes (value);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(val);
            }

            for (int i = 0; i < val.Length; i++, position++) {
				buffer [position] = val [i];
			}

			return buffer;
		}

		//<summary>
		// Converts Int into byte array.
		//</summary>
		private static byte[] convertToByteArray(int value){
			byte[] buffer = new byte[5];
			int position = 0;

			buffer [position] = (byte)2;
			position++;

			byte[] val = BitConverter.GetBytes (value);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(val);
            }

			for (int i = 0; i < val.Length; i++, position++) {
				buffer [position] = val [i];
			}

			return buffer;
		}

		//<summary>
		// Converts Long into byte array.
		//</summary>
		private static byte[] convertToByteArray(long value){
			byte[] buffer = new byte[9];
			int position = 0;

			buffer [position] = (byte)3;
			position++;

			byte[] val = BitConverter.GetBytes (value);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(val);
            }

            for (int i = 0; i < val.Length; i++, position++) {
				buffer [position] = val [i];
			}

			return buffer;
		}

		//<summary>
		// Converts Short into byte array.
		//</summary>
		private static byte[] convertToByteArray(short value){
			byte[] buffer = new byte[3];
			int position = 0;

			buffer [position] = (byte)4;
			position++;

			byte[] val = BitConverter.GetBytes (value);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(val);
            }

            for (int i = 0; i < val.Length; i++, position++) {
				buffer [position] = val [i];
			}

			return buffer;
		}

		//<summary>
		// Converts Char into byte array.
		//</summary>
		private static byte[] convertToByteArray(float value){
			byte[] buffer = new byte[5];
			int position = 0;

			buffer [position] = (byte)5;
			position++;

			byte[] val = BitConverter.GetBytes (value);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(val);
            }

            for (int i = 0; i < val.Length; i++, position++) {
				buffer [position] = val [i];
			}

			return buffer;
		}

		//<summary>
		// Converts Double into byte array.
		//</summary>
		private static byte[] convertToByteArray(double value){
			byte[] buffer = new byte[9];
			int position = 0;

			buffer [position] = (byte)6;
			position++;

			byte[] val = BitConverter.GetBytes (value);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(val);
            }

            for (int i = 0; i < val.Length; i++, position++) {
				buffer [position] = val [i];
			}

			return buffer;
		}

		//<summary>
		// Converts String into byte array.
		//</summary>
		private static byte[] convertToByteArray(String value){
			int strLength = value.Length + 5;
			byte[] buffer = new byte[strLength];
			int position = 0;

			buffer [position] = (byte)7;
			position++;
		
			byte[] size = BitConverter.GetBytes (value.Length);
            if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(size);
            }
            byte[] byteStr = Encoding.UTF8.GetBytes (value);

			for(int i = 0; i < size.Length; i++, position++) {
				buffer [position] = size [i];
			}

			for(int i = 0; i < byteStr.Length; i++, position++) {
				buffer [position] = byteStr [i];
			}

			return buffer;
		}

		//<summary>
		// Converts Bool into byte array.
		//</summary>
		private static byte[] convertToByteArray(bool value){
			byte[] array = new byte[2];
			array [0] = 8;
			array [1] = (byte)(value == true ? 1 : 0);
			return array;
		}

		//<summary>
		// Converts GUID into byte array.
		//</summary>
		private static byte[] convertToByteArray(Guid value){
			string uuid = value.ToString ();
			int strLength = uuid.Length + 5;
			byte[] buffer = new byte[strLength];
			int position = 0;

			buffer [position] = (byte)9;
			position++;

			byte[] size = BitConverter.GetBytes (uuid.Length);
             if (BitConverter.IsLittleEndian)
            {
                Array.Reverse(size);
            }
			byte[] byteStr = Encoding.UTF8.GetBytes (uuid);

			for(int i = 0; i < size.Length; i++, position++) {
				buffer [position] = size [i];
			}

			for(int i = 0; i < byteStr.Length; i++, position++) {
                //buffer [position] = byteStr [i];
                buffer[position] = Encoding.UTF8.GetBytes(uuid.Substring(i))[0];
			}

			return buffer;
		}

		//<summary>
		// This is used to convert the byte array received from a client into the method name and parameters for a method call.
		// The format is the same as the SendConversion method's output.
		//</summary>
		public static List<object> ReceiveConversion(byte[] array){
			List<object> paramaters = new List<object> ();
			for (int i = 0; i < array.Length; i++) {
				switch (array [i]) {
				case 1:
					paramaters.Add (convertToChar (AltimitArray.copyOfRange (array, i + 1, i + 3)));
					i += 2;
					break;
				case 2:
					paramaters.Add (convertToInt (AltimitArray.copyOfRange (array, i + 1, i + 5)));
					i += 4;
					break;
				case 3:
					paramaters.Add (convertToLong (AltimitArray.copyOfRange (array, i + 1, i + 9)));
					i += 8;
					break;
				case 4:
					paramaters.Add (convertToShort (AltimitArray.copyOfRange (array, i + 1, i + 3)));
					i += 2;
					break;
				case 5:
					paramaters.Add (convertToFloat (AltimitArray.copyOfRange (array, i + 1, i + 5)));
					i += 4;
					break;
				case 6:
					paramaters.Add (convertToDouble (AltimitArray.copyOfRange (array, i + 1, i + 9)));
					i += 8;
					break;
				case 7:
					int length = convertToInt (AltimitArray.copyOfRange (array, i + 1, i + 5));
					i += 5;
					try {
						paramaters.Add (convertToString (AltimitArray.copyOfRange( array, i, i + length)));
					} catch (Exception e) {
						Debug.LogWarning (e.ToString ());
					}
					i += length - 1;
 					break;
				case 8:
					paramaters.Add (convertToBool (array [i + 1]));
					i += 1;
					break;
				case 9:
					length = convertToInt (AltimitArray.copyOfRange (array, i + 1, i + 5));
					i += 5;
					try {
						paramaters.Add (convertToUUD (AltimitArray.copyOfRange (array, i, i + length)));
					} catch (Exception e) {
						Debug.LogWarning (e.ToString ());
					}
					i += length - 1;
					break;
				}
			}
				
			return paramaters;
		}

		//<summary>
		// Converts a byte array to a Char
		//</summary>
		private static char convertToChar(byte[] array){
			Array.Reverse (array);
			return BitConverter.ToChar (array, 0);
		}

		//<summary>
		// Converts a byte array to an Int
		//</summary>
		public static int convertToInt(byte[] array){
			Array.Reverse (array);
			return BitConverter.ToInt32 (array, 0);
		}

		//<summary>
		// Converts a byte array to a Long
		//</summary>
		private static long convertToLong(byte[] array){
			Array.Reverse (array);
			return BitConverter.ToInt64(array, 0);
		}

		//<summary>
		// Converts a byte array to a Short
		//</summary>
		private static short convertToShort(byte[] array){
			Array.Reverse (array);
			return BitConverter.ToInt16 (array, 0);
		}

		//<summary>
		// Converts a byte array to a Float
		//</summary>
		private static float convertToFloat(byte[] array){
			Array.Reverse (array);
			return BitConverter.ToSingle (array, 0);
		}

		//<summary>
		// Converts a byte array to a Double
		//</summary>
		private static double convertToDouble(byte[] array){
			Array.Reverse (array);
			return BitConverter.ToDouble (array, 0);
		}

		//<summary>
		// Converts a byte array to a String
		//</summary>
		private static string convertToString(byte[] array){
			string newString = "";

			foreach(byte cur in array){
				newString += Encoding.UTF8.GetString (new byte[] { cur });
			}

			return newString;
		}

		//<summary>
		// Converts a byte array to a Bool
		//</summary>
		private static bool convertToBool(byte array){

			return (Encoding.UTF8.GetString (new byte[] { array }) == "1");
		}

		//<summary>
		// Converts a byte array to a GUID
		//</summary>
		private static Guid convertToUUD(byte[] array){
			string newString = "";

			foreach(byte cur in array){
				newString += Encoding.UTF8.GetString (new byte[] { cur });
			}

			return new Guid (newString);
		}
	}
}
