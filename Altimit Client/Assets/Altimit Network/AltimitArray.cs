using UnityEngine;
using System.Collections;

namespace Altimit {
	
	public class AltimitArray : MonoBehaviour {

		//<summary>
		// Copies part of a byte array and returns that section.
		//</summary>
		public static byte[] copyOfRange(byte[] sourceArray, int start, int stop){
			int size = stop - start;
			byte[] temp = new byte[size];

			for (int i = 0; start < stop; i++, start++) {
				temp [i] = sourceArray [start];
			}

			return temp;
		}
	}
}