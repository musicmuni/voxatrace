//[voxatrace](../../../index.md)/[com.musicmuni.voxatrace.calibra](../index.md)/[CalibraLiveEval](index.md)/[close](close.md)

# close

[common]\
open fun [close](close.md)()

Release all resources.

## Ownership

- 
   CalibraLiveEval owns the detector and closes it here
- 
   LiveEvaluator owns refDetector (duplicate) and HPCP handles, closed via destroy()
- 
   studentDetector is passed to LiveEvaluator but NOT owned by it
- 
   LiveSession is owned by CalibraLiveEval, stopped here
- 
   Player and recorder are NOT owned - caller manages their lifecycle
