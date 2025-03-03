package com.pappt04.menzans.data

import com.pappt04.menzans.R

object UsefulLinks {

    /*--------------------------------------------------------------------------------------------*/
    data class linkContainer(val name: Uitext, val link: String)

    val usefulLinks = listOf(
        linkContainer(
            Uitext.StringResource(R.string.student_center_novi_sad),
            "https://www.scns.rs/"
        ),
        linkContainer(
            Uitext.StringResource(R.string.student_menza),
            "https://www.scns.rs/sektor-ishrane/"
        ),
        linkContainer(
            Uitext.StringResource(R.string.zavod_za_zdravstvenu_zastitu_studenata_novi_sad),
            "https://www.zzzzsns.co.rs"
        ),
        linkContainer(
            Uitext.StringResource(R.string.new_facebook_group_for_students_in_novi_sad),
            "https://www.facebook.com/groups/294889734815953"
        ),
        linkContainer(
            Uitext.StringResource(R.string.center_for_career_and_work),
            "https://www.infostud.com"
        ),
        linkContainer(
            Uitext.StringResource(R.string.university_of_novi_sad),
            "https://www.uns.ac.rs/index.php/"
        ),
        linkContainer(
            Uitext.StringResource(R.string.university_library),
            "https://www.uns.ac.rs/index.php/en/faculties/university-centres/central-library"
        ),
        linkContainer(
            Uitext.StringResource(R.string.moja_kartica_discounts_serbia),
            "https://mojakartica.rs"
        ),
        linkContainer(
            Uitext.StringResource(R.string.international_student_identity_card_discounts),
            "https://www.isic.org"
        ),
    )

    /*--------------------------------------------------------------------------------------------*/
    val allUnsAcRswebsites = listOf(
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_technical_sciences),
            "http://www.ftn.uns.ac.rs/691618389/fakultet-tehnickih-nauka"
        ),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_agriculture),
            "http://polj.uns.ac.rs"
        ),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_economics_in_subotica),
            "https://www.ef.uns.ac.rs"
        ),
        linkContainer(Uitext.StringResource(R.string.faculty_of_law), "https://pf.uns.ac.rs/rs/"),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_philosophy),
            "https://www.ff.uns.ac.rs"
        ),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_technology),
            "https://www.tf.uns.ac.rs/en#lat"
        ),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_medicine),
            "https://www.mf.uns.ac.rs/En/index_Eng.php"
        ),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_sciences),
            "https://www.pmf.uns.ac.rs/en/"
        ),
        linkContainer(
            Uitext.StringResource(R.string.academy_of_arts),
            "https://en.akademija.uns.ac.rs"
        ),
        linkContainer(
            Uitext.StringResource(R.string.faculty_of_sport_and_physical_education),
            "https://fspe.edu.rs"
        ),
    )
}