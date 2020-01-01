package com.eklitstudio.advokatkotilin.ui.konacni_trosak

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaIzracunatimTroskovimaRadnje
import com.eklitstudio.advokatkotilin.data.db.entity.relations.SlucajSaStrankama
import java.io.FileOutputStream
import java.io.IOException

class MyPrintDocumentAdapter(private var context: Context, private var slucajSaStrankama: SlucajSaStrankama,private var slucajSaIzracunatimTroskovimaRadnje: SlucajSaIzracunatimTroskovimaRadnje, private var ukupnaVrednostiSvihRadnji: Int): PrintDocumentAdapter() {

    private var pdfDocument: PdfDocument? = null
    private var visinaStranice: Int = 0
    private var sirinaStranice: Int = 0

    private var counterForListElements = 0


    /**
     * izracunaj broj broj strana koje se ocekuju
     */
    private fun izracunajBrojStranica(printAttributes: PrintAttributes, ukupanBrojStrana: Int): Int{

        var redovaPoStrani: Int = 20

        val pageSize = printAttributes.mediaSize
        if (pageSize!!.isPortrait) {
            redovaPoStrani = 20
        }

        return Math.ceil(ukupanBrojStrana.toDouble() / redovaPoStrani).toInt()
    }

    private var brojStranica = 1

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {

        pdfDocument = PrintedPdfDocument(context, newAttributes!!)
        visinaStranice = (newAttributes.mediaSize!!.heightMils / 1000 * 72)
        sirinaStranice = (newAttributes.mediaSize!!.widthMils / 1000 * 72)

        if (cancellationSignal!!.isCanceled) {
            callback!!.onLayoutCancelled()
            return
        }

        brojStranica = izracunajBrojStranica(newAttributes, slucajSaIzracunatimTroskovimaRadnje.izracunatiTroskoviRadnja.size)


        if (brojStranica > 0) {
            val builder = PrintDocumentInfo.Builder("print_output.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(brojStranica)
            val info = builder.build()
            callback!!.onLayoutFinished(info, true)
        } else {
            callback!!.onLayoutFailed("Page count is zero.")
        }
    }

    override fun onWrite(
        pages: Array<out PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {

        for (x in 0 until brojStranica){
            if (pageInRange(pages, x)){
                val newPage = PdfDocument.PageInfo.Builder(
                    sirinaStranice,
                    visinaStranice,
                    x).create()

                val page= pdfDocument?.startPage(newPage)

                if (cancellationSignal.isCanceled) {
                    callback.onWriteCancelled()
                    pdfDocument?.close()
                    pdfDocument = null
                    return
                }
                page?.let {
                    iscrtajPrvuStranicu(it, x)
                }

//
//                if (x == 0) {
//                    iscrtajPrvuStranicu(page, x)
//                } else {
//                    //todo ovo 20 12 19
//                    //drawOtherPages(page, i)
//                }
                pdfDocument?.finishPage(page)
            }
        }

        try {
            pdfDocument?.writeTo(
                FileOutputStream(
                    destination.fileDescriptor)
            )
        } catch (e: IOException) {
            callback.onWriteFailed(e.toString())
            return
        } finally {
            pdfDocument?.close()
            pdfDocument = null
        }

        callback.onWriteFinished(pages)
    }


    private fun iscrtajPrvuStranicu(
        page: PdfDocument.Page,
        pagenumber: Int) {

        //var pagenumber = pagenumber
        val canvas = page.canvas

        //pagenumber++ // Make sure page numbers start at 1

        var titleBaseLine: Float = 32f
        var leftMargin: Float = 54f

        //val leftMarginForName = 100
        val paint = TextPaint()
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        paint.textSize = 14f
        canvas.drawText(
            "advokat: imeiprezime",
            leftMargin.toFloat(),
            25.let { titleBaseLine += it; titleBaseLine }.toFloat(),
            paint
        )
        canvas.drawText(
            "adresa: adresa iz: mesto",
            leftMargin.toFloat(),
            25.let { titleBaseLine += it; titleBaseLine }.toFloat(),
            paint
        )
        canvas.drawText(
            "maticni broj 62324452, PIB: 3563622523 ",
            leftMargin.toFloat(),
            25.let { titleBaseLine += it; titleBaseLine }.toFloat(),
            paint
        )
        canvas.drawText(
            "email adresa: mymail@google.com",
            leftMargin.toFloat(),
            25.let { titleBaseLine += it; titleBaseLine }.toFloat(),
            paint
        )
        canvas.drawLine(
            50f,
            titleBaseLine + 15.toFloat(),
            600f,
            15.let { titleBaseLine += it; titleBaseLine }.toFloat(),
            paint
        )

        paint.textSize = 20f
        for (i in 0 until slucajSaStrankama.stranke.size) {
            canvas.drawText(
                "Stranka: " + slucajSaStrankama.stranke.get(i).ime_i_prezime + ", mesto stanovanja: " + slucajSaStrankama.stranke.get(i).adresa + ", iz: " + slucajSaStrankama.stranke.get(i).mesto,
                leftMargin.toFloat(),
                35.let {
                    titleBaseLine += it; titleBaseLine
                }.toFloat(),
                paint
            )
        }

        val troskoviBraniocaPaint = TextPaint()
        troskoviBraniocaPaint.textAlign = Paint.Align.CENTER
        troskoviBraniocaPaint.textSize = 22f
        val xPos = canvas.width / 2
        canvas.drawText(
            "TROSKOVI BRANIOCA",
            xPos.toFloat(),
            35.let { titleBaseLine += it; titleBaseLine }.toFloat(),
            troskoviBraniocaPaint
        )
        //titleBaseLine = 142;
        val resources: Resources = context.getResources()
        Log.d("Broj", titleBaseLine.toString())
        val scale = resources.displayMetrics.density
        var myStaticLayout: StaticLayout
        for (i in counterForListElements until slucajSaIzracunatimTroskovimaRadnje.izracunatiTroskoviRadnja.size) {
            if (visinaStranice - 35 > titleBaseLine) {
                counterForListElements++
                paint.isAntiAlias = true
                paint.textSize = 12f
                paint.color = -0x1000000
                val width = canvas.width - (16 * scale).toInt()
                val alignment =
                    Layout.Alignment.ALIGN_NORMAL
                val spacingMultiplier = 1f
                val spacingAddition = 0f
                val includePadding = false
                val nazivRadnje: String =
                    slucajSaIzracunatimTroskovimaRadnje.izracunatiTroskoviRadnja.get(i).naziv_radnje.toString()
                myStaticLayout = StaticLayout(
                    " - " + nazivRadnje + " dana " + slucajSaIzracunatimTroskovimaRadnje.izracunatiTroskoviRadnja.get(i).datum + " godine. ",
                    paint,
                    width,
                    alignment,
                    spacingMultiplier,
                    spacingAddition,
                    includePadding
                )
                //                    Canvas cs = page.getCanvas();
//                    cs.drawText( " " + listaOdradjenihRadnjiSaCenama.get(i).getCena_izracunate_jedinstvene_radnje(), leftMargin + 450,titleBaseLine+=40, paint);
                canvas.drawText(
                    " " + slucajSaIzracunatimTroskovimaRadnje.izracunatiTroskoviRadnja.get(i).cena,
                    leftMargin + 470.toFloat(),
                    45.let { titleBaseLine += it; titleBaseLine }.toFloat(),
                    paint
                )
                // canvas.drawText( " " + listaOdradjenihRadnjiSaCenama.get(i).getCena_izracunate_jedinstvene_radnje(), leftMargin + 450,titleBaseLine, paint);
                titleBaseLine = titleBaseLine - 10
                //val height = myStaticLayout.height.toFloat()
                canvas.save()
                canvas.translate(leftMargin.toFloat(), titleBaseLine.toFloat())
                myStaticLayout.draw(canvas)
                canvas.restore()
                //                    canvas.drawText(1+i + " - " + listaOdradjenihRadnjiSaCenama.get(i).getNaziv_radnje() + " dana " + listaOdradjenihRadnjiSaCenama.get(i).getDatum() + " godine." + listaOdradjenihRadnjiSaCenama.get(i).getCena_izracunate_jedinstvene_radnje(),leftMargin,titleBaseLine+=35, paint);
//                    canvas.drawText(String.valueOf(listaOdradjenihRadnjiSaCenama.get(i).getCena_izracunate_jedinstvene_radnje()),leftMargin + 450,titleBaseLine, paint);
            }
        }
        canvas.drawLine(
            50f,
            titleBaseLine + 40.toFloat(),
            600f,
            titleBaseLine + 40.toFloat(),
            paint
        )
        leftMargin = 350f
        if (counterForListElements == slucajSaIzracunatimTroskovimaRadnje.izracunatiTroskoviRadnja.size) {
            paint.textSize = 14f
            canvas.drawText(
                "Ukupan trosak svih radnji : $ukupnaVrednostiSvihRadnji" ,
                leftMargin.toFloat(),
                titleBaseLine + 90.toFloat(),
                paint
            )
        }
    }

    private fun pageInRange(
        pageRanges: Array<out PageRange>?,
        page: Int
    ): Boolean {
        for (i in pageRanges!!.indices) {
            if (page >= pageRanges[i].start && page <= pageRanges[i].end) return true}
        return false
    }
}