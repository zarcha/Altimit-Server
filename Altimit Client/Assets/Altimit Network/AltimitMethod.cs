using UnityEngine;
using System.Collections;
using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading;

namespace Altimit {
	public class AltimitMethod : MonoBehaviour {

		//<summary>
		// All methods marked with [AltimitRPC]
		//</summary>
		private static List<MethodBase> altimitMethods = new List<MethodBase> ();

		//<summary>
		// Get a list of all methods with [AltimitRPC]
		//</summary>
		public static void CompileAltimitMethods(){
			Debug.Log ("Compiling all Altimit methods...");

            Thread.Sleep(1000);

            foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies()){
				foreach (Type type in assembly.GetTypes()) {
					foreach (MethodBase method in type.GetMethods()) {
						if (method.GetCustomAttributes (typeof(AltimitRPC), true).Length > 0) {
							altimitMethods.Add (method);
						}
					}
				}
			}

			Debug.Log ("Finished compiling Altimit methods...");
		}

		//<summary>
		// Comparares a method's types with the variables in an object list
		//</summary>
		public static bool CompareTypes(ParameterInfo[] methodParams, object[] calledParams){
			if (methodParams.Length == calledParams.Length) {
				for (int i = 0; i < calledParams.Length; i++) {
					if (methodParams[i].ParameterType.Name != calledParams [i].GetType ().Name) {
						return false;
					}
				}
			}else{
				return false;
			}
				
			return true;
		}

		//<summary>
		// Ivokes a method by a string name and a the paramaters in a list
		//</summary>
		public static void CallAltimitMethod(string methodName, params object[] paramaters){
			foreach (MethodBase m in altimitMethods) {
				if (m.Name == methodName) {
					if (CompareTypes(m.GetParameters(), paramaters)) {
						object instance = Activator.CreateInstance (m.DeclaringType);
				
						try {
							m.Invoke (instance, paramaters);

							return;
						} catch (MissingMethodException e) {
							Debug.Log (e.ToString ());
						}
					
					}
				}
			}
		}
	}
}