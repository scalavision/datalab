package bio

import zio.test.*
import zio.test.Assertion.*

object IntervalSpec:
  val suite1 =  suite("interval spec")(
    test("simple test case"){
      // Mobile elements and coordinates
      // https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3383450/
      //https://arnaudceol.wordpress.com/2014/09/18/chromosome-coordinate-systems-0-based-1-based/ 
      val dna = "ACTGACTG"
      val test = "TGAC"
      val zeroBasedInclusive = dna(2,5) 
      val zeroBasedExclusive = dna(2,6) // Bed, half-open
      val oneBasedInclusive = dna(3,6) // Ensemble, one-based closed system
      val oneBasedExclusive = dna(3,7)

      assert(1)(equalTo(1))
    }
  )
