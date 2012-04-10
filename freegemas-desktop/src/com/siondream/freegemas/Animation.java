package com.siondream.freegemas;

import java.lang.reflect.Method;
import java.util.HashMap;

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

    public enum Attribute {None, Alpha, Position, AlphaPosition};
    
    private int _numAttributes;
    private int _duration;
    private int _delay;
    private int _time;
    private int _initial[];
    private int _final[];
    private int _change[];
    private float _current[];
    private Type _type;
    private Method _method; 

    private static HashMap<Type, Method> _animMethods = null;
    
    public static void init() {
    	_animMethods = new HashMap<Type, Method>();
    	
    	Class<Animation> animClass = Animation.class;
    	
    	try {
			_animMethods.put(Type.EaseInQuad, animClass.getMethod("easeInQuad", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseOutQuad, animClass.getMethod("easeOutQuad", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseInCubic, animClass.getMethod("easeInCubic", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseOutCubic, animClass.getMethod("easeOutCubic", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseInOutCubic, animClass.getMethod("easeInOutCubic", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseInQuart, animClass.getMethod("easeInQuart", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseOutQuart, animClass.getMethod("easeOutQuart", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseInOutQuart, animClass.getMethod("easeInOutQuart", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.EaseOutBack, animClass.getMethod("easeOutBack", new Class[]{float.class, float.class, float.class, float.class}));
			_animMethods.put(Type.Linear, animClass.getMethod("easeLinear", new Class[]{float.class, float.class, float.class, float.class}));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public Animation(int numAttributes, int duration, Type type, int delay) {
    	_numAttributes = numAttributes;
    	_duration = duration;
    	_type = type;
    	_delay = delay;
    	
    	if (_animMethods != null) {
    		_method = _animMethods.get(_type);
    	}
    	
    	if (_method == null) {
    		Class<Animation> animClass = Animation.class;
    		try{
    		_method = animClass.getMethod("easeLinear",
    									  new Class[]{float.class, float.class, float.class, float.class});
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	_initial = new int[_numAttributes];
    	_final = new int[_numAttributes];
    	_change = new int[_numAttributes];
    	_current = new float[_numAttributes];
    	
    	for (int i = 0; i < _numAttributes; ++i) {
    		_initial[i] = _final[i] = _change[i] = 0;
    		_current[i] = 0.0f; 
    	}
    }
    
    public int getNumAttributes() {
    	return _numAttributes;
    }
    
    public void restart() {
    	_time = 0;
    }
    
    public void end() {
    	_time = _duration + _delay;
    	update(false);
    }
    
    public boolean isFinished() {
    	return _time >= _duration + _delay;
    }
    
    public float getAttribute(int i) {
    	if (i < 0 || i >= _numAttributes) {
    		return 0.0f;
    	}
    	
    	return _current[i];
    }
    
    public void setAttribute(int i, int v1, int v2) {
    	if (i < 0 || i >= _numAttributes) {
    		return ;
    	}
    	
    	_initial[i] = v1;
    	_final[i] = v1;
    	_change[i] = v2 - v1;
    	_current[i] = v1;
    }
    
    public void setInitial(int i, int v) {
    	if (i < 0 || i >= _numAttributes) {
    		return ;
    	}
    	
    	_initial[i] = v;
    	_change[i] = _final[i] - v;
    	_current[i] = v;
    }
    
    public void setFinal(int i, int v) {
    	if (i < 0 || i >= _numAttributes) {
    		return ;
    	}
    	
    	_final[i] = v;
    	_change[i] = v - _initial[i];
    }
    
    public void reverse() {
    	int a;
        for (int i = 0; i < _numAttributes; ++i) {
	    	a = _initial[i];
	    	_initial[i] = _final[i];
	    	_final[i] = a;
	    	_change[i] = _final[i] - _initial[i];
        }
    }
    
    public Type getType() {
    	return _type;
    }
    
    public boolean setType(Type type) {
    	_type = type;
    	
    	_method = _animMethods.get(_type);
    	
    	if (_method == null) {
    		Class<Animation> animClass = Animation.class;
    		try{
    		_method = animClass.getMethod("easeLinear",
    									  new Class[]{float.class, float.class, float.class, float.class});
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	return _method != null;
    }
    
    public int getDuration() {
    	return _duration;
    }
    
    public void setDuration(int duration) {
    	_duration = duration;
    }
    
    public int getDelay() {
    	return _delay; 
    }
    
    public void setDelay(int delay) {
    	_delay = delay;
    }
    
    public void update(boolean a) {
    	if (_time >= _duration + _delay) {
    		for (int i = 0; i < _numAttributes; ++i) {
    		    _current[i] = _final[i];
    		}
    		
    		return;
    	}
	    else if (_time >= _delay) {
			for (int i = 0; i < _numAttributes; ++i) {
				try {
					_current[i] = (Float)_method.invoke(null, _time - _delay, _initial[i], _change[i], _duration);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    }
	    
	    if (a) {
	    	++_time;
	    }
    }
    
    public static float easeLinear(float t, float b, float c, float d) {
    	return c * t / d + b;
    }

    public static float easeInQuad (float t, float b, float c, float d) {
    	t /= d;
    	
        return c * t * t + b;
    }
    
    public static float easeOutQuad(float t, float b, float c, float d) {
    	 t /= d;
    	 
    	 return -c * (t) * (t - 2) + b;
    }
    
    public static float easeInOutQuad(float t, float b, float c, float d) {
    	t /= d / 2;
        if (t < 1) {
        	return c / 2 * t * t + b;
        }
        else {
        	--t;
        	return -c / 2 * ((t) * (t - 2) - 1) + b;
        }
    }
    
    public static float easeInCubic(float t, float b, float c, float d) {
    	t /= d;
    	
        return c * (t) * t * t + b;
    }
    public static float easeOutCubic(float t, float b, float c, float d) {
    	 t = t / d - 1;
    	 
    	 return c * (t * t * t + 1) + b;
    }
    public static float easeInOutCubic(float t, float b, float c, float d) {
    	t /= d / 2;
        if ((t) < 1) {
        	return c / 2 * t * t * t + b;
        }
        else {
	    	t-=2;
	    	return c / 2 * ((t) * t * t + 2) + b;
        }
    }

    public static float easeInQuart(float t, float b, float c, float d) {
    	t /= d;
    	
        return c * (t) * t * t * t + b;
    }
    
    public static float easeOutQuart(float t, float b, float c, float d) {
    	t = t /d - 1;
    	
        return -c * ((t) * t * t * t - 1) + b;
    }
    
    public static float easeInOutQuart(float t, float b, float c, float d) {
        t /= d / 2;
        if ((t) < 1) {
        	return c / 2 * t * t * t * t + b;
        }
        else {
	    	t -= 2;
	    	return -c / 2 * ((t) * t * t * t - 2) + b;
        }
    }

    public static float easeOutBack(float t, float b, float c, float d) {
    	float s = 1.3f;
        t = t / d - 1;
        return c * (t * t * ((s + 1) * t + s) + 1) + b;
    }
}
