package bio.vcf.parser

enum VariantIdType:
  // Submitted SNP ID assigned by dbSNP or EVA.
  case ssID(value: String)
  // rs334	Reference SNP ID assigned by dbSNP or EVA. ssIDs of the same variant type that colocalise are combined to give an rsID for that locus.
  case rsID(value: String)

/*
  case HomozygousReference
  case HomozygousAlternate
  case Heterozygous
  case Other(value: String)
 */

/** TODO: add rest of the variant id types and incorporate into the model ssID
  * ss335 Submitted SNP ID assigned by dbSNP or EVA. rsID rs334 Reference SNP ID
  * assigned by dbSNP or EVA. ssIDs of the same variant type that colocalise are
  * combined to give an rsID for that locus. //http://varnomen.hgvs.org/ HGVS*
  * ENST00000366667.4:c.803T>C Expresses the location of the variant in terms of
  * a transcript or protein. COSMIC ID COSM1290 ID assigned by COSMIC for
  * somatic variants. HGMD CD830010 ID assigned by HGMD to variants known to be
  * associated with human inherited diseases. ClinVar RCV000016573 ID assigned
  * to dbSNP or dbVar/DGVa annotated variants, linking them to human health.
  * UniProt VAR_010085 ID assigned by UniProt for reviewed human. DGVa variant
  * call essv8691751 Submitted structural variant ID assigned by DGVa. Variants
  * are shared with dbVar. dbVar variant call nssv1602417 Submitted structural
  * variant ID assigned by dbVar. Variants are shared with DGVa. DGVa variant
  * region esv3364878 Variant region variant ID assigned by DGVa. Overlapping
  * submitted variants (essv and nssv) are combined into a single variant
  * region. The boundaries of a variant region may not match those of the
  * submitted variants, which can vary. dbVar variant region nsv916030 Variant
  * region variant ID assigned by dbVar. Overlapping submitted variants (essv
  * and nssv) are combined into a single variant region. The boundaries of a
  * variant region may not match those of the submitted variants, which can
  * vary.
  */
