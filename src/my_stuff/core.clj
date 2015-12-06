(ns my-stuff.core
  (:require [seesaw.core :refer :all])
  (:gen-class))

(def exer8
	"Exercício 11.8"
	(frame 
		:title "Align"
		:on-close :dispose
		:content (vertical-panel
					:items [(flow-panel
								:items [(make-widget [:fill-h 15])
										(vertical-panel 
											:items [(checkbox :text "Snap to Grid")
													(checkbox :text "Show Grid")])
										(vertical-panel
											:items [(flow-panel :items ["X:" (text :text "8" :columns 4)])
													(flow-panel :items ["Y:" (text :text "8" :columns 4)])])
										(grid-panel
											:rows 3
											:columns 1
											:vgap 5
											:items [(button :text "Ok")
													(button :text "Cancel")
													(button :text "Help")])
										(make-widget [:fill-h 15])])
							(make-widget [:fill-v 15])])))

(def exer9
	"Exercício 11.9"
	(frame
		:title "Calculator"
		:on-close :dispose
		:content (border-panel
					:north (text)
					:center (grid-panel
								:rows 4
								:columns 4
								:items (for [i ["7" "8" "9" "/"
												"4" "5" "6" "*"
												"1" "2" "3" "-"
												"0" "," "=" "+"]]
											(button :text i))))))

(def exer10
	"Exercício 11.10"
	(frame
		:title "ColorSelect"
		:on-close :dispose
		:content (border-panel
					:north (combobox :model ["RED" "GREEN" "BLUE"])
					:center (flow-panel
								:items [(checkbox :text "Background")
										(checkbox :text "Foreground")])
					:south (flow-panel
								:items [(button :text "Ok")
										(button :text "Cancel")]))))

(def exer11
	"Exercício 11.11"
	(frame
		:title "Printer"
		:on-close :dispose
		:content (vertical-panel
					:items [(horizontal-panel
								:items [(make-widget [:fill-h 15])
										(border-panel
											:north "Printer: MyPrinter"
											:center (flow-panel
														:items [(vertical-panel
																	:items [(checkbox :text "Image")
																			(checkbox :text "Text")
																			(checkbox :text "Code")])
																(vertical-panel
																	:items [(radio :text "Selecion")
																			(radio :text "All" :selected? true)
																			(radio :text "Applet")])])
											:south (flow-panel
														:items ["Print Quality: "
																(combobox :model ["High" "Low"])
																(checkbox :text "Print to File")]))
										(grid-panel
											:rows 4
											:columns 1
											:vgap 5
											:items [(button :text "OK")
													(button :text "Cancel")
													(button :text "Setup...")
													(button :text "Help")])
										(make-widget [:fill-h 15])])
							(make-widget [:fill-v 10])])))

(def exer12
	"Exercício 11.12"
	(frame
		:title "Conversor de temperatura"
		:on-close :dispose
		:content (flow-panel
					:items ["Temperatura em ºC:"
							(text
								:columns 4
								:listen [:action
											(fn [e] (text! 
														(select (to-root e) [:#flabel])
														(str (+ 32 (* 1.8 (read-string (text e)))))))])
							(label :id :flabel :text "Temperatura em ºF")])))

(defn desc "retorna a descrição (doc) da var" [v] (get (meta v) :doc))

(def my-menu
	(frame
		:title "Menu de opções"
		:on-close :exit
		:content (vertical-panel
					:items 	(for [exer [#'exer8 #'exer9 #'exer10 #'exer11 #'exer12]]
								(button
									:text (desc exer)
									:listen [:action
												(fn [_]
													(-> (var-get exer)
														pack!
														show!))])))))


(defn -main
	"(Alguns) Exercicios do Capítulo 11 do Livro do Deitel, 6ª Edição"
	[& args]
	(invoke-later
		(-> my-menu
			pack!
			show!)))
