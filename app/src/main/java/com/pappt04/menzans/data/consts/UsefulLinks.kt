package com.pappt04.menzans.data.consts

import com.pappt04.menzans.R
import com.pappt04.menzans.models.Uitext

object UsefulLinks {
    data class LinkContainer(
        val name: Uitext,
        val link: String,
    )

    val usefulLinks =
        listOf(
            LinkContainer(
                Uitext.StringResource(R.string.student_center_novi_sad),
                "https://www.scns.rs/",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.student_menza),
                "https://www.scns.rs/sektor-ishrane/",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.zavod_za_zdravstvenu_zastitu_studenata_novi_sad),
                "https://www.zzzzsns.co.rs",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.new_facebook_group_for_students_in_novi_sad),
                "https://www.facebook.com/groups/294889734815953",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.center_for_career_and_work),
                "https://www.infostud.com",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.university_of_novi_sad),
                "https://www.uns.ac.rs/index.php/",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.university_library),
                "https://www.uns.ac.rs/index.php/en/faculties/university-centres/central-library",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.moja_kartica_discounts_serbia),
                "https://mojakartica.rs",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.international_student_identity_card_discounts),
                "https://www.isic.org",
            ),
        )

    // --------------------------------------------------------------------------------------------
    val allUnsAcRswebsites =
        listOf(
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_technical_sciences),
                "http://www.ftn.uns.ac.rs/691618389/fakultet-tehnickih-nauka",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_agriculture),
                "http://polj.uns.ac.rs",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_economics_in_subotica),
                "https://www.ef.uns.ac.rs",
            ),
            LinkContainer(Uitext.StringResource(R.string.faculty_of_law), "https://pf.uns.ac.rs/rs/"),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_philosophy),
                "https://www.ff.uns.ac.rs",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_technology),
                "https://www.tf.uns.ac.rs/en#lat",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_medicine),
                "https://www.mf.uns.ac.rs/En/index_Eng.php",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_sciences),
                "https://www.pmf.uns.ac.rs/en/",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.academy_of_arts),
                "https://en.akademija.uns.ac.rs",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.faculty_of_sport_and_physical_education),
                "https://fspe.edu.rs",
            ),
        )

    val topbarLinks =
        listOf(
            LinkContainer(
                Uitext.StringResource(R.string.privacy_policy),
                "https://apollo4.duckdns.org/menzaapi/webapi/privacyPolicy",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.data_disclosure),
                "https://apollo4.duckdns.org/menzaapi/webapi/dataDeletionDisclosure",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.open_source_code),
                "https://github.com/Pappt04/MenzaNS",
            ),
            LinkContainer(
                Uitext.StringResource(R.string.google_play),
                AppConfig.APP_STORE_URL,
            ),
        )
}

