package com.altimit_server;

import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * All method related methods.
 */
class AltimitMethod {

    /**
     * Methods maked with @AltimitCmd
     */
    static List<Method> allMethods = null;

    /**
     * Retrieves all methods in the Altimit Server package.
     */
    static void AltimitMethodCompile(){
        allMethods = GetAltimitMethods("com.altimit_server");
    }

    /**
     * Dynamically invokes a specified method with its paramaters.
     * @param methodName The name of the method to be called. Must be in the allMethods list.
     * @param args All parameters to be used with the invoke.
     */
    static void CallAltimitMethod(String methodName, Object... args){
        Object obj = null;

        for(Method m : allMethods){
            if(Objects.equals(m.getName(), methodName)){
                try{
                    Class c = m.getDeclaringClass();
                    obj = c.newInstance();

                    m.invoke(obj, args);
                    return;
                }catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }
    }

    /**
     * Gets all methods within a specified package that is marked with @AltimitCmd
     * @param packageName Name of package to compile the list within
     * @return List of methods.
     */
    private static List<Method> GetAltimitMethods(String packageName){
        System.out.println("Compiling list of methods...");
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner(false), new ResourcesScanner()).setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))).filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        List<Method> AltimitCmdMethods = new LinkedList<Method>();

        Object[] allClasses2 = reflections.getSubTypesOf(Object.class).toArray();

        for (Object c : allClasses2) {
            Class curClass = (Class)c;
            Method[] allMethods = curClass.getDeclaredMethods();
            for(Method m : allMethods){
                if(m.isAnnotationPresent(AltimitCmd.class)) {
                    AltimitCmdMethods.add(m);
                }
            }
        }

        return AltimitCmdMethods;
    }
}
