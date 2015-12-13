(ns my-stuff.exer18
  (:require [seesaw.core :refer :all])
  (:require [seesaw.graphics :refer :all])
  (:require [seesaw.color :refer [color]])
  (:require [seesaw.bind :as b])
  (:require [clojure.string :refer [join]])
)

(defrecord Shape [type style])
    
(defn create-specified-shape [shapeType x y w h]
  (case shapeType
    "rectangle" (rect x y w h)
    "ellipse" (ellipse x y w h)))

(defn create-shape-from [shape x y w h]
  (cond
    (instance? java.awt.geom.Rectangle2D shape) (create-specified-shape "rectangle" x y w h)
    (instance? java.awt.geom.Ellipse2D   shape) (create-specified-shape "ellipse"   x y w h)))

(defn mouse-resize-shape [mouseEvent shape]
	(let [shapeType (:type shape)]
    (assoc shape
           :type (create-shape-from shapeType
                       (.getX shapeType)
                       (.getY shapeType)
                       (- (.getX mouseEvent) (.getX shapeType))
                       (- (.getY mouseEvent) (.getY shapeType))))))
                          
(def _content
  (border-panel
      :north (flow-panel
                 :items [(button   :id :undo     :text "Undo")
                         (button   :id :clear    :text "Clear")
                         (combobox :id :cbcolor  :model ["red" "green" "blue"])
                         (combobox :id :cbtype   :model ["rectangle" "ellipse"])
                         (checkbox :id :ckfilled :text "Filled")])
      :center (border-panel
                :west [:fill-v 300]
                :north [:fill-h 300]
                :center (canvas :id :drawPanel))
        :south (label :id :status :text "Status")))

(def current       (atom nil))
(def shapes        (atom (list)))
(def selectedColor (atom "red"))
(def selectedType  (atom "rectangle"))
(def isFilled      (atom false))

(b/bind (select _content [:#cbcolor])  selectedColor)
(b/bind (select _content [:#cbtype])   selectedType)
(b/bind (select _content [:#ckfilled]) isFilled)

(config! (select _content [:#drawPanel])
       :paint (fn [_ g2d] 
                (doseq [shape (if-let [currentShape @current] ; if current isn't nil, it's drawn too.
                                (conj @shapes currentShape)
                                @shapes)]
                  (draw g2d (:type shape) (:style shape)))))
                       
(defn show-coordinates [e]
  (config! (select _content [:#status]) :text (join ["(" (.getX e) ", " (.getY e) ")"])))

(listen (select _content [:#drawPanel])
        :mouse-pressed (fn [e]
                          (swap! current (fn [_]
                                           (Shape. 
                                             (create-specified-shape @selectedType (.getX e) (.getY e) 1 1)
                                             (style
                                               :foreground @selectedColor
                                               :background (if (true? @isFilled) @selectedColor nil)))))
                          (-> e .getSource .repaint))
         :mouse-released (fn [e]
                           (swap! current
                                  (fn [currentShape]
                                    (mouse-resize-shape e currentShape))) ; resize current shape according to mouse position
                           (swap! shapes #(conj %1 @current))             ; add the freshly drawn shape into shapes collection
                           (swap! current (fn [_] nil))                   ; reset the current shape (no shape is in 'drawing process' for now)
                           (-> e .getSource .repaint))
         :mouse-dragged  (fn [e] 
                           (swap! current
                                  (fn [currentShape]
                                    (mouse-resize-shape e currentShape)))
                           (show-coordinates e)
                           (-> e .getSource .repaint))
         :mouse-moved    (fn  [e] (show-coordinates e)))

(listen (select _content [:#undo])
        :action (fn [_]
                  (swap! shapes #(rest %1)) ; take every shape but the first one (idk why the last added item is considered the first item)
                  (.repaint (select _content [:#drawPanel]))))

(listen (select _content [:#clear])
        :action (fn [_]
                  (swap! shapes (fn [_] (list)))
                  (.repaint (select _content [:#drawPanel]))))

(def exer18
  "Exercício 11.18"
  (frame
    :title "Java Drawings"
    :on-close :dispose
    :content _content))