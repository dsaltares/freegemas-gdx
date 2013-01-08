package com.siondream.freegemas;

public class Animation {
	/// Kinds of animation
    public enum Type {EaseInQuad,
    				  EaseOutQuad,
    				  EaseInOutQuad,
    				  EaseInCubic,
    				  EaseOutCubic,
    				  EaseInOutCubic, 
    				  EaseInQuart, 
    				  EaseOutQuart, 
    				  EaseInOutQuart,
    				  EaseOutBack,
    				  Linear};
    
    public static float easeLinear(double t, float b, float c, double d) {
    	return (float)(c * t / d + b);
    }

    public static float easeInQuad (double t, float b, float c, double d) {
    	t /= d;
    	
        return (float)(c * t * t + b);
    }
    
    public static float easeOutQuad(double t, float b, float c, double d) {
    	 t /= d;
    	 
    	 return (float)(-c * (t) * (t - 2) + b);
    }
    
    public static float easeInOutQuad(double t, float b, float c, double d) {
    	t /= d / 2;
        if (t < 1) {
        	return (float)(c / 2 * t * t + b);
        }
        else {
        	--t;
        	return (float)(-c / 2 * ((t) * (t - 2) - 1) + b);
        }
    }
    
    public static float easeInCubic(double t, float b, float c, double d) {
    	t /= d;
    	
        return (float)(c * (t) * t * t + b);
    }
    public static float easeOutCubic(double t, float b, float c, double d) {
    	 t = t / d - 1;
    	 
    	 return (float)(c * (t * t * t + 1) + b);
    }
    public static float easeInOutCubic(double t, float b, float c, double d) {
    	t /= d / 2;
        if ((t) < 1) {
        	return (float)(c / 2 * t * t * t + b);
        }
        else {
	    	t-=2;
	    	return (float)(c / 2 * ((t) * t * t + 2) + b);
        }
    }

    public static float easeInQuart(double t, float b, float c, double d) {
    	t /= d;
    	
        return (float)(c * (t) * t * t * t + b);
    }
    
    public static float easeOutQuart(double t, float b, float c, double d) {
    	t = t /d - 1;
    	
        return (float)(-c * ((t) * t * t * t - 1) + b);
    }
    
    public static float easeInOutQuart(double t, float b, float c, double d) {
        t /= d / 2;
        if ((t) < 1) {
        	return (float)(c / 2 * t * t * t * t + b);
        }
        else {
	    	t -= 2;
	    	return (float)(-c / 2 * ((t) * t * t * t - 2) + b);
        }
    }

    public static float easeOutBack(double t, float b, float c, double d) {
    	float s = 1.3f;
        t = t / d - 1;
        return (float)(c * (t * t * ((s + 1) * t + s) + 1) + b);
    }
}
